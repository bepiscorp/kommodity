package tyrell.callisto.blob.model

import tyrell.callisto.base.definition.LibraryApi
import tyrell.callisto.blob.enums.BlobState
import tyrell.callisto.blob.model.metadata.BlobMetadata

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public data class BlobModel(

    public var data: BlobBinaryData,

    public var state: BlobState = BlobState.INITIAL,

    public val identifier: IdentifierData = IdentifierData(),

    public val metadata: BlobMetadata = BlobMetadata(),
) : BlobModelData()
