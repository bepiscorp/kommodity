package tyrell.callisto.test

import tyrell.callisto.base.definition.LibraryApi

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public class TestException : RuntimeException {

    private lateinit var messageClosure: () -> String

    override val message: String? by lazy {
        when {
            (super.message != null) -> super.message
            ::messageClosure.isInitialized -> messageClosure()
            else -> null
        }
    }

    public constructor(message: String, cause: Throwable) : super(message, cause)

    public constructor(message: String) : super(message)

    public constructor(cause: Throwable) : super(cause)

    public constructor(
        messageClosure: () -> String,
        cause: Throwable,
    ) : super(null, cause) {
        this.messageClosure = messageClosure
    }
}
