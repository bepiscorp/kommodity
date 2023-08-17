package tyrell.callisto.data.type

import tyrell.callisto.base.definition.LibraryApi

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public interface NameAware {

    public val name: String?
}
