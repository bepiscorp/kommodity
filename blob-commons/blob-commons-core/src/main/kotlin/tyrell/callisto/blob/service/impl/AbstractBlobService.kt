package tyrell.callisto.blob.service.impl

import mu.KLogger
import org.springframework.beans.factory.BeanNameAware
import org.springframework.beans.factory.DisposableBean
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import tyrell.callisto.base.definition.LibraryApi
import tyrell.callisto.base.kotlin.properties.LoggingDelegates
import tyrell.callisto.blob.enums.BlobState
import tyrell.callisto.blob.enums.CompressionType
import tyrell.callisto.blob.model.BlobModel
import tyrell.callisto.blob.model.BlobUploadModel
import tyrell.callisto.blob.model.IdentifierData
import tyrell.callisto.blob.model.metadata.BlobMetadata
import tyrell.callisto.blob.service.BlobExtensionManager
import tyrell.callisto.blob.service.BlobService
import java.time.Clock
import java.util.UUID
import kotlin.properties.Delegates

/**
 * Abstract class [AbstractBlobService] provides:
 * - final implementations of [BlobService] declarations;
 * - utility declarations for classes who extend [AbstractBlobService].
 *
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public abstract class AbstractBlobService : BlobService, InitializingBean, DisposableBean, BeanNameAware {

    protected val logger: KLogger by LoggingDelegates.instanceLogger()

    protected var name: String by Delegates.vetoable(this::class.simpleName!!) { _, oldValue, _ ->
        val isDefaultName = (oldValue == this::class.simpleName!!)
        check(isDefaultName) { "Redefining of service name is forbidden" }
        true
    }

    // TODO: Remove hardcode
    protected val clock: Clock = Clock.systemDefaultZone()

    @set:[Lazy Autowired]
    protected lateinit var extensionManager: BlobExtensionManager

    final override fun setBeanName(name: String) {
        this.name = name
    }

    final override fun afterPropertiesSet() {
        onInitialize()
    }

    final override fun destroy() {
        onDestroy()
    }

    final override suspend fun upload(uploadModel: BlobUploadModel): BlobModel {
        val model: BlobModel = toBlobModel(uploadModel)

        val modifiedModel: BlobModel = model.deepCopy().apply { state = BlobState.UPLOADING }
        extensionManager.beforeUpload(initialModel = model, modifiedModel = modifiedModel)

        doUpload(modifiedModel)
        extensionManager.onUpload(uploadedModel = modifiedModel)

        return modifiedModel
    }

    final override suspend fun load(identifier: IdentifierData): BlobModel? {
        val model: BlobModel? = doLoad(identifier)
        if (model == null) {
            extensionManager.onLoad(loadedModel = null)
            return null
        }

        val modifiedModel: BlobModel = model.deepCopy()
        extensionManager.beforeLoad(initialModel = model, modifiedModel = modifiedModel)

        val loadedModel: BlobModel = modifiedModel.deepCopy().also { checkBlobPurity(it) }
        extensionManager.onLoad(loadedModel = loadedModel)

        return loadedModel
    }

    protected abstract suspend fun doUpload(model: BlobModel)

    protected abstract suspend fun doLoad(identifier: IdentifierData): BlobModel?

    protected open fun checkBlobPurity(model: BlobModel): Unit = model.run {
        if (metadata.compression.type != CompressionType.NONE) {
            error(
                """
                    Blob compression with type [${metadata.compression.type}] is present.
                    Provide BlobExtensionManager in the context that will uncompress blobs with mentioned compression.
                """.trimIndent(),
            )
        }
    }

    protected open fun toBlobModel(uploadModel: BlobUploadModel): BlobModel = BlobModel(
        data = uploadModel.data,
        state = BlobState.INITIAL,
        identifier = uploadModel.identifier.copy(),
        metadata = BlobMetadata(
            description = uploadModel.description.copy(),
        ),
    ).also { // Fill defaults
        it.identifier.apply {
            if (id == null) { // Generate id if absent
                id = UUID.randomUUID()
            }
        }

        it.metadata.description.apply {
            if (fileName == null) { // Generate file name if absent
                fileName = it.identifier.id.toString() + ".dat"
            }
        }
    }

    protected fun BlobModel.deepCopy(): BlobModel = copy(
        identifier = identifier.copy(),
        metadata = with(metadata) {
            BlobMetadata(
                description = description.copy(),
                signature = signature.copy(),
                compression = compression.copy(),
            )
        },
    )
}
