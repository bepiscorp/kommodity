package tyrell.callisto.hibernate.entity.companion

import tyrell.callisto.base.definition.LibraryApi

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
@Suppress("PropertyName", "VariableNaming")
public interface NameAwareEntityColumns : CodeAwareEntityColumns {

    public val NAME: String
        @JvmSynthetic get() = "name"

    public val DESCRIPTION: String
        @JvmSynthetic get() = "description"
}
