package tyrell.callisto.blob.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import tyrell.callisto.base.definition.InternalLibraryApi
import tyrell.callisto.blob.model.BlobModel
import tyrell.callisto.blob.service.BlobExtension
import tyrell.callisto.blob.service.BlobExtensionManager

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@InternalLibraryApi
@Lazy @Component
internal class BlobExtensionManagerImpl : BlobExtensionManager {

    private var extensions: List<BlobExtension> = emptyList()

    @Suppress("RedundantVisibilityModifier")
    @Lazy
    @Autowired(required = false)
    public fun setExtensions(extensions: List<BlobExtension>) {
        this.extensions = extensions
    }

    override suspend fun beforeUpload(initialModel: BlobModel, modifiedModel: BlobModel) {
        for (extension: BlobExtension in extensions) {
            extension.beforeUpload(initialModel, modifiedModel)
        }
    }

    override fun onUpload(uploadedModel: BlobModel) {
        for (extension: BlobExtension in extensions) {
            extension.onUpload(uploadedModel)
        }
    }

    override suspend fun beforeLoad(initialModel: BlobModel, modifiedModel: BlobModel) {
        for (extension: BlobExtension in extensions.asReversed()) {
            extension.beforeLoad(initialModel, modifiedModel)
        }
    }

    override fun onLoad(loadedModel: BlobModel?) {
        for (extension: BlobExtension in extensions.asReversed()) {
            extension.onLoad(loadedModel)
        }
    }
}
