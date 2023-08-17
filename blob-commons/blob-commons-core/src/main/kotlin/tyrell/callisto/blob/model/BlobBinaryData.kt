package tyrell.callisto.blob.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import tyrell.callisto.base.definition.DelicateLibraryApi
import tyrell.callisto.base.definition.ExperimentalLibraryApi
import tyrell.callisto.base.definition.InternalLibraryApi
import tyrell.callisto.base.definition.LibraryApi
import java.io.ByteArrayOutputStream

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public sealed interface BlobBinaryData {

    public fun asByteArrayFlow(): Flow<ByteArray>
}

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@ExperimentalLibraryApi @DelicateLibraryApi
public suspend fun BlobBinaryData.readBytes(): ByteArray =
    ByteArrayOutputStream(DEFAULT_BUFFER_SIZE).use { outputStream: ByteArrayOutputStream ->
        asByteArrayFlow()
            .onEach { bytes: ByteArray -> outputStream.writeBytes(bytes) }
            .collect()
        outputStream.toByteArray()
    }

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@ExperimentalLibraryApi @DelicateLibraryApi
public fun BlobBinaryData.readBytesBlocking(): ByteArray = runBlocking { readBytes() }

@InternalLibraryApi
public class ByteArrayBinaryData(private val data: ByteArray) : BlobModelData(), BlobBinaryData {

    override fun asByteArrayFlow(): Flow<ByteArray> = flowOf(data)
}

@InternalLibraryApi
public class FlowBinaryData(private val flow: Flow<ByteArray>) : BlobModelData(), BlobBinaryData {

    override fun asByteArrayFlow(): Flow<ByteArray> = flow
}

@InternalLibraryApi
public object EmptyBinaryData : BlobModelData(), BlobBinaryData {

    override fun asByteArrayFlow(): Flow<ByteArray> = error("Cannot resolve ByteArray flow from EmptyBinaryData")
}
