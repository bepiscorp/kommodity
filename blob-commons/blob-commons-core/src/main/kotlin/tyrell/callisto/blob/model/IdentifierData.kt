package tyrell.callisto.blob.model

import tyrell.callisto.base.definition.LibraryApi
import java.util.UUID

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public data class IdentifierData(

    public var id: UUID? = null,
) : BlobModelData()
