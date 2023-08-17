package tyrell.callisto.base.exception

import tyrell.callisto.base.StandardResponseCode
import tyrell.callisto.base.definition.DelicateLibraryApi
import tyrell.callisto.base.definition.LibraryApi
import tyrell.callisto.base.l10n.Localizer
import tyrell.callisto.base.messaging.model.LocalizableMessage
import tyrell.callisto.base.messaging.model.ResponseCode
import tyrell.callisto.base.messaging.model.ResponseData
import tyrell.callisto.base.messaging.model.asResponseData
import java.util.Locale
import kotlin.properties.Delegates

/**
 * The [ServiceException] class is the superclass of all custom exception across the system.
 *
 * @author Mikhail Gostev
 * @since 0.0.1
 * @see RuntimeException
 * @see ResponseData
 */
@LibraryApi
public class ServiceException : RuntimeException {

    /**
     * The [ResponseData] that describes the details of the failure.
     */
    public val data: ResponseData

    // region ================ Base constructors ========================================

    /**
     * Constructs a new [ServiceException] without detail message and cause.
     */
    public constructor() : this(DEFAULT_RESPONSE_DATA)

    /**
     * Constructs a new [ServiceException] with the specified detail message but without cause.
     *
     * Use [ServiceException(ResponseCode, Object...)] or [ServiceException(ResponseCode, LocalizableMessage)]
     * instead when possible.
     *
     * @param message the detail message. Saved for later retrieval by the [Throwable.message] method.
     *
     * @see RuntimeException(String)
     */
    public constructor(message: String) : super(message) {
        this.data = DEFAULT_RESPONSE_DATA.copy { this.message = LocalizableMessage(message) }
    }

    /**
     * Constructs a new [ServiceException] with the specified detail message and cause.
     *
     * Use [#ServiceException(Throwable, ResponseCode, Object...)]
     * or [#ServiceException(Throwable, ResponseCode, LocalizableMessage)] instead when possible.
     *
     * @param message the detail message. Saved for later retrieval by the [Throwable.message] method.
     * @param cause the cause. Saved for later retrieval by the [Throwable.cause] method.
     *
     * @see RuntimeException(String, Throwable)
     */
    public constructor(message: String, cause: Throwable) : super(message, cause) {
        this.data = DEFAULT_RESPONSE_DATA.copy { this.message = LocalizableMessage(message) }
    }

    /**
     * Constructs a new [ServiceException] with the specified [cause] and empty or inherited detail message.
     * This constructor is useful for runtime exceptions that are little more than wrappers for other throwable.
     *
     * @param cause the cause. The cause is saved for later retrieval by the [Throwable.cause] method.
     *
     * @see RuntimeException(Throwable)
     */
    public constructor(cause: Throwable) : super(cause) {
        this.data = DEFAULT_RESPONSE_DATA
    }

    /**
     * Constructs a new [ServiceException] with the specified detail message, cause, suppression enabled or disabled,
     * and writable stack trace enabled or disabled.
     *
     * @param message the detail message. Saved for later retrieval by the [Throwable.message] method.
     * @param cause the cause. Saved for later retrieval by the [Throwable.cause] method.
     * @param enableSuppression whether suppression is enabled or disabled
     * @param writableStackTrace whether the stack trace should be writable
     *
     * @see RuntimeException(String, Throwable, boolean, boolean)
     */
    public constructor(
        message: String,
        cause: Throwable,
        enableSuppression: Boolean,
        writableStackTrace: Boolean,
    ) : super(message, cause, enableSuppression, writableStackTrace) {
        this.data = DEFAULT_RESPONSE_DATA
    }

    // endregion

    // region ================ Response data constructors ========================================

    /**
     * Constructs a new [ServiceException] with the specified [cause] and [ResponseData].
     *
     * @param data the response data.
     */
    private constructor(cause: Throwable, data: ResponseData) : super(
        getPlainMessage(data.message.asLocalizableMessage()),
        cause,
    ) {
        this.data = data
    }

    /**
     * Constructs a new [ServiceException] with the specified [ResponseData].
     *
     * @param data the response data.
     */
    public constructor(data: ResponseData) : super(getPlainMessage(data.message.asLocalizableMessage())) {
        this.data = data
    }

    /**
     * Constructs a new [ServiceException] for specified response code and message parameters.
     *
     * @param responseCode the response code
     * @param messageParams message parameters
     *
     * @see ResponseCode.asResponseData
     */
    public constructor(responseCode: ResponseCode, vararg messageParams: Any?) : this(
        data = responseCode.asResponseData { message = LocalizableMessage(it.messageCode, *messageParams) },
    )

    /**
     * Constructs a new [ServiceException] for specified response code and message parameters and cause.
     *
     * @param cause the cause. Saved for later retrieval by the [Throwable.cause] method.
     * @param responseCode the response code
     * @param messageParams message parameters
     *
     * @see ResponseCode.asResponseData
     */
    public constructor(cause: Throwable, responseCode: ResponseCode, vararg messageParams: Any?) : this(
        cause = cause,
        data = responseCode.asResponseData { message = LocalizableMessage(it.messageCode, *messageParams) },
    )

    /**
     * Constructs a new [ServiceException] for specified response code and overridden message.
     *
     * @param responseCode the response code
     * @param message the message
     *
     * @see ResponseCode.asResponseData
     */
    public constructor(responseCode: ResponseCode, message: LocalizableMessage) : this(
        data = responseCode.asResponseData { this.message = message },
    )

    /**
     * Constructs a new [ServiceException] for specified response code and overridden message.
     *
     * @param cause the cause. Saved for later retrieval by the [Throwable.cause] method.
     * @param responseCode the response code
     * @param message the message
     *
     * @see ResponseCode.asResponseData
     */
    public constructor(cause: Throwable, responseCode: ResponseCode, message: LocalizableMessage) : this(
        cause = cause,
        data = responseCode.asResponseData { this.message = message },
    )

    // endregion

    @LibraryApi
    public object LocalizerHolder {

        /**
         * Localizer **MUST*** be configured exactly once. Localizer **MUST NOT** be set to `null`.
         *
         * On setting localizer configurer **MUST** synchronize on [ServiceException] class instance monitor.
         */
        @JvmStatic
        @DelicateLibraryApi
        public var localizer: Localizer? by Delegates.vetoable(null) { _, oldValue, newValue ->
            checkNotNull(newValue) { "Localizer cannot be set to null" }
            check(oldValue == null) { "Redefining of localizer is forbidden" }
            true
        }
    }

    private companion object {

        private val DEFAULT_RESPONSE_DATA: ResponseData = StandardResponseCode.STD0003_ERROR.asResponseData()

        @JvmStatic
        private fun getPlainMessage(message: LocalizableMessage): String {
            val localizer = LocalizerHolder.localizer ?: return buildPlainMessage(message)
            val messageLocales: List<Locale> = localizer.getSupportedLocales()
                .filter { locale -> localizer.isLocaleSupported(locale, message.code) }
                .takeIf(List<*>::isNotEmpty) ?: return buildPlainMessage(message)
            return localizer.localize(messageLocales.first(), message.code)
        }

        private fun buildPlainMessage(message: LocalizableMessage): String =
            buildString(capacity = DEFAULT_BUFFER_SIZE) {
                append(message.code)
                if (message.parameters.isNotEmpty()) {
                    message.parameters.joinTo(
                        buffer = this,
                        prefix = "[", postfix = "]",
                        transform = ::stringifyValue,
                    )
                }
            }

        private fun stringifyValue(value: Any?): String = when (value) {
            null -> "null"
            is String -> value
            is LocalizableMessage -> buildPlainMessage(value)
            is Collection<*> -> value.asSequence().map(::stringifyValue).joinToString(prefix = "[", postfix = "]")
            is Array<*> -> value.asSequence().map(::stringifyValue).joinToString(prefix = "[", postfix = "]")
            else -> value.toString()
        }
    }
}
