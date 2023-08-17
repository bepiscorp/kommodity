package tyrell.callisto.hibernate.entity.companion

import tyrell.callisto.base.definition.LibraryApi

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
@Suppress("PropertyName", "VariableNaming")
public interface AuditAwareEntityColumns : BaseEntityColumns {

    public val CREATION_DATE: String
        @JvmSynthetic get() = "creationDate"

    public val AUDIT_DATE: String
        @JvmSynthetic get() = "auditDate"

    public val AUDIT_STATE: String
        @JvmSynthetic get() = "auditState"

    public val REVISION: String
        @JvmSynthetic get() = "revision"
}
