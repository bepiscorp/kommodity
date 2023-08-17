package tyrell.callisto.api.model

import tyrell.callisto.base.definition.LibraryApi
import tyrell.callisto.base.definition.TransferredModel
import tyrell.callisto.base.type.NamedReference
import java.util.UUID

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public data class NamedReferenceDto(

    override val id: UUID,

    override val name: String? = null,
) : NamedReference<UUID>, TransferredModel
