package tyrell.callisto.blob.model.metadata

import tyrell.callisto.blob.model.BlobModelData
import tyrell.callisto.blob.model.BlobSignature

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
public data class SignatureData(

    var dataSignature: BlobSignature? = null,
) : BlobModelData()
