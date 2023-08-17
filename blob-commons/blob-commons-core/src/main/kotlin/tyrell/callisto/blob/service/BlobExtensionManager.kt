package tyrell.callisto.blob.service

import tyrell.callisto.base.definition.LibraryApi
import tyrell.callisto.blob.model.BlobModel

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 * @see BlobExtension
 */
@LibraryApi
public interface BlobExtensionManager {

    /**
     * Called when [BlobModel] was not yet persisted to the storage and been processed yet
     *
     * @see BlobService.upload
     */
    public suspend fun beforeUpload(initialModel: BlobModel, modifiedModel: BlobModel)

    /**
     * Called right before returning persisted [BlobModel] to the caller
     *
     * @see BlobService.upload
     */
    public fun onUpload(uploadedModel: BlobModel)

    /**
     * Called when [BlobModel] was loaded from the storage and additional processing is required for it
     *
     * @see BlobService.load
     */
    public suspend fun beforeLoad(initialModel: BlobModel, modifiedModel: BlobModel)

    /**
     * Called right before returning [BlobModel] to the caller
     *
     * @see BlobService.load
     */
    public fun onLoad(loadedModel: BlobModel?)
}
