package tyrell.callisto.base.resource.exception

/**
 * This exception is thrown by `ResourceManager` if an error occurs while loading resource.
 * It wraps any inner exception thrown.
 *
 * @author Mikhail Gostev
 * @since 0.0.1
 * @see tyrell.callisto.base.resource.ResourceManager
 */
public class ResourceLoadingException : ResourceProcessingException {

    public constructor(message: String) : super(message)

    public constructor(message: String, cause: Throwable) : super(message, cause)

    public constructor(cause: Throwable) : super(cause)
}
