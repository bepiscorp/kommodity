package tyrell.callisto.data.type

import tyrell.callisto.base.definition.LibraryApi
import tyrell.callisto.data.enum.AuditState
import java.time.LocalDateTime

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public interface AuditAware {

    public var creationDate: LocalDateTime?

    public var auditDate: LocalDateTime?

    public var auditState: AuditState

    public var revision: Long?
}
