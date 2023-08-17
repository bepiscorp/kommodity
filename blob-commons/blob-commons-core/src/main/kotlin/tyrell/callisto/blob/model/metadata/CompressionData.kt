package tyrell.callisto.blob.model.metadata

import tyrell.callisto.blob.enums.CompressionType
import tyrell.callisto.blob.model.BlobModelData

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
public data class CompressionData(

    var type: CompressionType = CompressionType.NONE,

    var actualSize: Long = -1,
) : BlobModelData()
