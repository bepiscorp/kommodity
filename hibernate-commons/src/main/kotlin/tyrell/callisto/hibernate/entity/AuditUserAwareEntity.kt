package tyrell.callisto.hibernate.entity

import org.hibernate.envers.AuditOverride
import org.hibernate.envers.Audited
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.LastModifiedBy
import tyrell.callisto.base.definition.LibraryApi
import tyrell.callisto.data.type.AuditUserAware
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.MappedSuperclass

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@[LibraryApi Audited MappedSuperclass]
@AuditOverride(forClass = AuditAwareEntity::class)
public abstract class AuditUserAwareEntity<ID : Serializable> : AuditAwareEntity<ID>, AuditUserAware<ID> {

    @get:CreatedBy
    @get:Column(name = "creator_user_id")
    override var creatorUserId: ID? = null

    @get:LastModifiedBy
    @get:Column(name = "audit_user_id")
    override var auditUserId: ID? = null

    public constructor() : super()

    public companion object {

        public const val CREATOR_USER_ID: String = "creatorUserId"

        public const val AUDIT_USER_ID: String = "auditUserId"
    }
}
