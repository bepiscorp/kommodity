package tyrell.callisto.data.type

import tyrell.callisto.base.definition.LibraryApi
import java.io.Serializable

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public interface AuditUserAware<ID> where ID : Any, ID : Serializable {

    public var creatorUserId: ID?

    public var auditUserId: ID?
}
