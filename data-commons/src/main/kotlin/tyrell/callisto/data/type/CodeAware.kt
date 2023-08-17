package tyrell.callisto.data.type

import tyrell.callisto.base.definition.LibraryApi
import java.io.Serializable

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public interface CodeAware<out C> where C : Any, C : Serializable {

    public val code: C?
}
