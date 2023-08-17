package tyrell.callisto.data.enum

import tyrell.callisto.base.definition.LibraryApi
import java.io.Serializable

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public interface DbEnum : Serializable {

    public val dbKey: String
}
