package tyrell.callisto.blob.hibernate.dao.jpa

import tyrell.callisto.base.definition.LibraryApi
import tyrell.callisto.blob.hibernate.entity.BlobEntity
import tyrell.callisto.hibernate.dao.jpa.JpaAuditAwareDao
import java.util.UUID

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public interface BlobEntityDao : JpaAuditAwareDao<BlobEntity, UUID>
