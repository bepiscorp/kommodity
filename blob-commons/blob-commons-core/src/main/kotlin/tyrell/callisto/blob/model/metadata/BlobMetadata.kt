package tyrell.callisto.blob.model.metadata

import tyrell.callisto.base.definition.LibraryApi
import tyrell.callisto.base.definition.SerializedAttributes
import tyrell.callisto.blob.model.BlobModelData

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public data class BlobMetadata(

    val description: DescriptionData = DescriptionData(),

    val signature: SignatureData = SignatureData(),

    val compression: CompressionData = CompressionData(),
) : SerializedAttributes, BlobModelData()
