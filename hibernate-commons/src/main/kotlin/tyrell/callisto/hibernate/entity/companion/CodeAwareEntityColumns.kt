package tyrell.callisto.hibernate.entity.companion

import tyrell.callisto.base.definition.LibraryApi

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
@Suppress("PropertyName", "VariableNaming")
public interface CodeAwareEntityColumns : AuditUserAwareEntityColumns {

    public val CODE: String
        @JvmSynthetic get() = "code"
}
