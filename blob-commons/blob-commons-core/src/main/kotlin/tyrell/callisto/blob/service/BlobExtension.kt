package tyrell.callisto.blob.service

import tyrell.callisto.base.definition.LibraryApi
import tyrell.callisto.blob.model.BlobModel

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 * @see BlobExtensionManager
 */
@LibraryApi
public interface BlobExtension {

    public fun onLoad(loaded: BlobModel, response: BlobModel) {
        // Nothing to do
    }

    public suspend fun beforeUpload(initialModel: BlobModel, modifiedModel: BlobModel) {
        // Nothing to do
    }

    public fun onUpload(uploadedModel: BlobModel) {
        // Nothing to do
    }

    public suspend fun beforeLoad(initialModel: BlobModel, modifiedModel: BlobModel) {
        // Nothing to do
    }

    public fun onLoad(loadedModel: BlobModel?) {
        // Nothing to do
    }
}
