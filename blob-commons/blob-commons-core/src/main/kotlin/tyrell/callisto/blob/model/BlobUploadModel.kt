package tyrell.callisto.blob.model

import tyrell.callisto.base.definition.LibraryApi
import tyrell.callisto.blob.model.metadata.DescriptionData

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public class BlobUploadModel(

    public var data: BlobBinaryData,

    public val identifier: IdentifierData = IdentifierData(),

    public val description: DescriptionData = DescriptionData(),
) : BlobModelData()
