package tyrell.callisto.base.resource.exception

import tyrell.callisto.base.definition.LibraryApi

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public open class ResourceProcessingException : RuntimeException {

    public constructor(message: String, cause: Throwable) : super(message, cause)

    public constructor(message: String) : super(message)

    public constructor(cause: Throwable) : super(cause)
}
