package tyrell.callisto.hibernate.dao.jpa

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.NoRepositoryBean
import tyrell.callisto.base.definition.LibraryApi
import tyrell.callisto.hibernate.entity.AuditAwareEntity
import java.io.Serializable

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
@NoRepositoryBean
public interface JpaAuditAwareDao<T : AuditAwareEntity<*>, ID : Serializable> : JpaRepository<T, ID>
