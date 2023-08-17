package tyrell.callisto.blob.model.metadata

import tyrell.callisto.base.definition.LibraryApi
import tyrell.callisto.blob.model.BlobModelData

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public data class DescriptionData(

    var fileName: String? = null,

    var size: Long? = null,
) : BlobModelData()
