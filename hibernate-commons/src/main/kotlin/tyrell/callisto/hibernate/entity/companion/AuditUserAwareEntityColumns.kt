package tyrell.callisto.hibernate.entity.companion

import tyrell.callisto.base.definition.LibraryApi

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
@Suppress("PropertyName", "VariableNaming")
public interface AuditUserAwareEntityColumns : AuditAwareEntityColumns {

    public val CREATOR_USER_ID: String
        @JvmSynthetic get() = "creatorUserId"

    public val AUDIT_USER_ID: String
        @JvmSynthetic get() = "auditUserId"
}
