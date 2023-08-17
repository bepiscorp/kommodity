package tyrell.callisto.base.type

import tyrell.callisto.base.definition.LibraryApi
import java.io.Serializable

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public interface Reference<out ID : Serializable> {

    public val id: ID
}
