package tyrell.callisto.blob.hibernate.entity

import org.hibernate.envers.AuditOverride
import org.hibernate.envers.NotAudited
import tyrell.callisto.base.definition.InternalLibraryApi
import tyrell.callisto.blob.model.metadata.BlobMetadata
import tyrell.callisto.hibernate.entity.AuditAwareEntity
import tyrell.callisto.hibernate.entity.companion.AuditAwareEntityColumns
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Lob
import javax.persistence.Table

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@InternalLibraryApi
@AuditOverride(isAudited = false)
@Entity
@Table(name = BlobEntity.TABLE_NAME)
public class BlobEntity : AuditAwareEntity<UUID> {

    private var id: UUID? = null

    @get:[NotAudited Lob]
    @get:Column(name = "data", nullable = false)
    public var data: ByteArray? = null

    @get:Column(name = "metadata", nullable = false)
    public var metadata: BlobMetadata? = null

    public constructor()

    public constructor(id: UUID) {
        this.id = id
    }

    public constructor(
        data: ByteArray,
        metadata: BlobMetadata,

        id: UUID,
    ) {
        this.data = data
        this.metadata = metadata

        this.id = id
    }

    public constructor(value: BlobEntity) {
        this.id = value.id

        this.data = value.data
        this.metadata = value.metadata

        this.creationDate = value.creationDate
        this.auditDate = value.auditDate
        this.auditState = value.auditState
        this.revision = value.revision
    }

    @Id
    @Column(name = "id", nullable = false)
    override fun getId(): UUID? = id

    override fun setId(id: UUID) {
        this.id = id
    }

    public companion object : AuditAwareEntityColumns {

        public const val TABLE_NAME: String = "clst_blob"

        public const val DATA: String = "data"

        public const val METADATA: String = "metadata"
    }
}
