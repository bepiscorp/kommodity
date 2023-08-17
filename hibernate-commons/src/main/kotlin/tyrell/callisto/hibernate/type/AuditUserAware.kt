package tyrell.callisto.hibernate.type

import tyrell.callisto.base.definition.LibraryApi

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@Deprecated(
    message = "Audit state enum has moved into data commons",
    level = DeprecationLevel.WARNING,
    replaceWith = ReplaceWith(expression = "tyrell.callisto.data.type.AuditUserAware"),
)
@LibraryApi
public typealias AuditUserAware<ID> = tyrell.callisto.data.type.AuditUserAware<ID>
