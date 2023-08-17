package tyrell.callisto.blob.service

import tyrell.callisto.base.definition.LibraryApi
import tyrell.callisto.blob.model.BlobModel
import tyrell.callisto.blob.model.BlobUploadModel
import tyrell.callisto.blob.model.IdentifierData

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 * @see IdentifierData
 * @see BlobUploadModel
 * @see BlobModel
 */
@LibraryApi
public interface BlobService {

    /**
     * Called on blob service start
     */
    public fun onInitialize() {
        // Nothing to do
    }

    /**
     * Called on blob service destroying
     */
    public fun onDestroy() {
        // Nothing to do
    }

    /**
     * Perform uploading blob into storage
     *
     * @return uploaded blob model
     */
    public suspend fun upload(uploadModel: BlobUploadModel): BlobModel

    /**
     * Load blob from the storage by its identifier
     *
     * @return found blob or `null` if it was not found
     */
    public suspend fun load(identifier: IdentifierData): BlobModel?

    /**
     * Remove blob by its identifier. Implementation of this method is optional.
     *
     * @return `true` if blob was deleted, `false` otherwise
     */
    public suspend fun remove(identifier: IdentifierData): Boolean {
        throw UnsupportedOperationException()
    }
}
