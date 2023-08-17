package tyrell.callisto.data.type

import tyrell.callisto.base.definition.LibraryApi
import java.io.Serializable

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public interface WithId<out ID> where ID : Any, ID : Serializable {

    public fun getId(): ID?
}
