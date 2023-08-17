package tyrell.callisto.blob.model

import tyrell.callisto.base.definition.LibraryApi
import tyrell.callisto.blob.enums.SignatureType

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public class BlobSignature(

    public val type: SignatureType,

    public val value: String,
) : BlobModelData()
