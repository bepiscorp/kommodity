package tyrell.callisto.hibernate.entity

import org.hibernate.annotations.Where
import org.hibernate.envers.Audited
import org.hibernate.envers.RevisionNumber
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import tyrell.callisto.base.definition.LibraryApi
import tyrell.callisto.data.enum.AuditState
import tyrell.callisto.data.type.AuditAware
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Basic
import javax.persistence.Column
import javax.persistence.MappedSuperclass
import javax.persistence.Transient

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@[LibraryApi Audited MappedSuperclass]
@Where(clause = "audit_state <> 'R")
public abstract class AuditAwareEntity<ID : Serializable> : BaseEntity<ID>, AuditAware {

    @get:[Basic CreatedDate]
    @get:Column(name = "creation_date")
    override var creationDate: LocalDateTime? = null

    @get:[Basic LastModifiedDate]
    @get:Column(name = "audit_date")
    override var auditDate: LocalDateTime? = null

    @get:Basic
    @get:Column(name = "audit_state", nullable = false)
    override var auditState: AuditState = AuditState.ACTIVE

    @get:[Transient RevisionNumber]
    override var revision: Long? = null

    public constructor() : super()

    public companion object {

        public const val CREATION_DATE: String = "creationDate"

        public const val AUDIT_DATE: String = "auditDate"

        public const val AUDIT_STATE: String = "auditState"

        public const val REVISION: String = "revision"
    }
}
