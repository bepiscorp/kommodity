package tyrell.callisto.base.type

import tyrell.callisto.base.definition.LibraryApi
import java.io.Serializable

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public interface NamedReference<ID : Serializable> {

    public val id: ID

    public val name: String?
}
