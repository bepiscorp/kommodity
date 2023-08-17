package tyrell.callisto.blob.filesystem.service

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.asPublisher
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.runInterruptible
import kotlinx.coroutines.withContext
import org.reactivestreams.Publisher
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.core.io.buffer.DefaultDataBufferFactory
import org.springframework.util.FileSystemUtils.deleteRecursively
import tyrell.callisto.base.StandardResponseCode
import tyrell.callisto.base.definition.InternalLibraryApi
import tyrell.callisto.base.exception.ServiceException
import tyrell.callisto.blob.enums.BlobState
import tyrell.callisto.blob.model.BlobModel
import tyrell.callisto.blob.model.EmptyBinaryData
import tyrell.callisto.blob.model.FlowBinaryData
import tyrell.callisto.blob.model.IdentifierData
import tyrell.callisto.blob.model.metadata.BlobMetadata
import tyrell.callisto.blob.service.impl.AbstractBlobService
import tyrell.callisto.data.util.ThreadSafeObjects
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import java.nio.file.attribute.PosixFilePermission
import java.util.EnumSet
import kotlin.coroutines.CoroutineContext
import kotlin.io.path.bufferedWriter
import kotlin.io.path.deleteIfExists
import kotlin.io.path.exists
import kotlin.io.path.isDirectory
import kotlin.io.path.readBytes
import kotlin.io.path.setPosixFilePermissions

// TODO: Check for inappropriate blocking calls
/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@InternalLibraryApi
public class FileSystemBlobService(

    private val blobDirectory: Path,

    private val deleteOnExit: Boolean,
) : AbstractBlobService() {

    override fun onDestroy() {
        if (deleteOnExit) {
            assert(blobDirectory.isDirectory())
            deleteRecursively(blobDirectory)
        }
    }

    override suspend fun doUpload(model: BlobModel) {
        val metadataPath: Path = resolveMetadataPath(model.identifier)
        val dataPath: Path = resolveDataPath(model.identifier)

        withContext(Dispatchers.IO + createDisposingCoroutineExceptionHandler(metadataPath, dataPath)) {
            // 1. Check preconditions
            coroutineScope {
                launch { checkMetadataFileExistence(metadataPath, mustExist = false) }
                launch { checkDataFileExistence(dataPath, mustExist = false) }
            }

            // 2. Write into data file
            launch {
                val bufferPublisher: Publisher<DataBuffer> = model.data.asByteArrayFlow()
                    .map { DefaultDataBufferFactory.sharedInstance.wrap(it) }
                    .asPublisher(currentCoroutineContext())

                DataBufferUtils
                    .write(bufferPublisher, dataPath, StandardOpenOption.CREATE_NEW)
                    .awaitSingleOrNull()

                runInterruptible { dataPath.setPosixFilePermissions(BLOB_FILE_PERMISSIONS) }
            }

            // 3. Write into metadata file
            launch {
                metadataPath
                    .bufferedWriter(options = arrayOf(StandardOpenOption.CREATE_NEW))
                    .use { writer -> runInterruptible { METADATA_MAPPER.writeValue(writer, model.metadata) } }

                runInterruptible { metadataPath.setPosixFilePermissions(BLOB_FILE_PERMISSIONS) }
            }
        }
    }

    override suspend fun doLoad(identifier: IdentifierData): BlobModel? {
        val metadataPath: Path = resolveMetadataPath(identifier)
        val dataPath: Path = resolveDataPath(identifier)

        return withContext(Dispatchers.IO) closure@{
            // 1. Check preconditions
            if (!checkMetadataFileExistence(metadataPath)) {
                return@closure null
            }
            val dataExists: Boolean = checkDataFileExistence(dataPath)

            // 2. Load metadata
            val deferredMetadata = async {
                val metadataBytes: ByteArray = runInterruptible { metadataPath.readBytes() }
                METADATA_MAPPER.readValue(metadataBytes, BlobMetadata::class.java)
            }

            // 3. Load data
            val deferredBinaryData = if (dataExists) {
                async {
                    val byteArrayFlow: Flow<ByteArray> = DataBufferUtils
                        .read(dataPath, DefaultDataBufferFactory.sharedInstance, DEFAULT_BUFFER_SIZE)
                        .asFlow()
                        .map { dataBuffer -> dataBuffer.asInputStream().readBytes() }
                    FlowBinaryData(byteArrayFlow)
                }
            } else {
                CompletableDeferred(EmptyBinaryData)
            }

            return@closure BlobModel(
                data = deferredBinaryData.await(),
                state = if (dataExists) BlobState.UPLOADED else BlobState.REMOVED,
                identifier = identifier,
                metadata = deferredMetadata.await(),
            )
        }
    }

    private suspend fun checkMetadataFileExistence(metadataPath: Path, mustExist: Boolean? = null): Boolean {
        val metadataExists: Boolean = runInterruptible { metadataPath.exists() }
        if ((mustExist != null) && (metadataExists != mustExist)) {
            // TODO add message:
            //  "Blob was not loaded as the metadata file [$metadataPath]
            //  had presence [$metadataExists] in the filesystem"
            throw ServiceException(StandardResponseCode.STD0002_SERVICE_ERROR).let(logger::throwing)
        }
        return metadataExists
    }

    private suspend fun checkDataFileExistence(dataPath: Path, mustExist: Boolean? = null): Boolean {
        val dataExists: Boolean = runInterruptible { dataPath.exists() }
        if ((mustExist != null) && (dataExists != mustExist)) {
            // TODO add message:
            //  "Blob was not loaded as the metadata file [$dataPath] had presence [dataExists] in the filesystem"
            throw ServiceException(StandardResponseCode.STD0002_SERVICE_ERROR).let(logger::throwing)
        }
        return dataExists
    }

    private fun resolveMetadataPath(identifierData: IdentifierData): Path =
        blobDirectory.resolve(generateFileName(identifierData, extension = "meta"))

    private fun resolveDataPath(identifierData: IdentifierData): Path =
        blobDirectory.resolve(generateFileName(identifierData, extension = "dat"))

    private fun createDisposingCoroutineExceptionHandler(vararg paths: Path): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { coroutineContext: CoroutineContext, throwable: Throwable ->
            for (path in paths) {
                try {
                    path.deleteIfExists()
                } catch (ignored: Throwable) {
                    logger.catching(ignored)
                }
            }
            throw ServiceException(throwable, StandardResponseCode.STD0002_SERVICE_ERROR)
                .let(logger::throwing)
        }
    }

    private fun generateFileName(identifierData: IdentifierData, extension: String): String =
        identifierData.id?.let { id -> "$id.$extension" }
            ?: throw ServiceException("Property [id] was null in IdentifierData")
                .let(logger::throwing)

    private companion object {

        private val BLOB_FILE_PERMISSIONS: EnumSet<PosixFilePermission> = EnumSet.of(
            PosixFilePermission.OWNER_READ,
            PosixFilePermission.OWNER_WRITE,
        )

        private val METADATA_MAPPER: ObjectMapper = ThreadSafeObjects.JsonMappers.plainMapper
    }
}
