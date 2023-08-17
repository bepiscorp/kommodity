package tyrell.callisto.base.messaging.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import tyrell.callisto.base.definition.ExperimentalLibraryApi
import tyrell.callisto.base.definition.TransferredModel
import tyrell.callisto.base.io.DEFAULT_CONTAINER_CAPACITY
import tyrell.callisto.base.messaging.enums.ResponseType
import java.util.function.Consumer

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@ExperimentalLibraryApi
public inline fun ResponseCode.asResponseData(
    crossinline transform: ((ResponseData.BuilderScope).(ResponseCode) -> Unit) = { },
): ResponseData {
    val instance: ResponseCode = this
    return ResponseData.build {
        code = instance.code
        reposeType = instance.responseType
        message = LocalizableMessage(instance.messageCode)

        transform(this, instance)
    }
}

/**
 * See also [Microsoft REST API Guidelines. Response Object](https://github.com/microsoft/api-guidelines/blob/vNext/Guidelines.md#errorresponse--object)
 *
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@ExperimentalLibraryApi
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ResponseData private constructor(

    public val code: String,

    public val reposeType: ResponseType,

    public val message: LocalizableMessageView,

    public val details: List<ResponseData>,

    public val data: Map<String, Any>,
) : TransferredModel {

    @JvmSynthetic
    public fun copy(closure: BuilderScope.() -> Unit): ResponseData {
        val instance: ResponseData = this
        val scope = BuilderScope()

        scope.apply {
            code = instance.code
            reposeType = instance.reposeType
            messageView = instance.message
            data.putAll(instance.data)

            closure(this)
        }

        return ResponseData(
            code = scope.code,
            reposeType = scope.reposeType,
            message = checkNotNull(scope.messageView),
            details = scope.details,
            data = scope.data.toMap(),
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ResponseData) return false

        if (code != other.code) return false
        if (reposeType != other.reposeType) return false
        if (message != other.message) return false
        if (details != other.details) return false
        if (data != other.data) return false

        return true
    }

    override fun hashCode(): Int {
        var result = code.hashCode()
        result = 31 * result + reposeType.hashCode()
        result = 31 * result + message.hashCode()
        result = 31 * result + details.hashCode()
        result = 31 * result + data.hashCode()
        return result
    }

    override fun toString(): String = "ResponseData(" +
        "code='$code'," +
        "reposeType=$reposeType," +
        "message=$message," +
        "details=$details," +
        "data=$data" +
        ")"

    public companion object {

        @JvmStatic
        @JvmSynthetic
        public fun build(closure: BuilderScope.() -> Unit): ResponseData {
            val scope = BuilderScope().apply(closure)
            return ResponseData(
                code = scope.code,
                reposeType = scope.reposeType,
                message = checkNotNull(scope.messageView),
                details = scope.details,
                data = scope.data.toMap(),
            )
        }

        @JvmStatic
        public fun build(consumer: Consumer<Builder>): ResponseData {
            val builder = Builder().apply { consumer.accept(this) }
            return ResponseData(
                code = builder.code,
                reposeType = builder.reposeType,
                message = builder.message,
                details = builder.details,
                data = builder.data,
            )
        }
    }

    public class BuilderScope internal constructor() {

        public lateinit var code: String

        public lateinit var reposeType: ResponseType

        public var message: LocalizableMessage? = null
            set(value) {
                field = value
                this.messageView = value?.asView()
            }

        public var messageView: LocalizableMessageView? = null

        public val details: MutableList<ResponseData> = ArrayList(DEFAULT_CONTAINER_CAPACITY)

        public val data: MutableMap<String, Any> = HashMap(DEFAULT_CONTAINER_CAPACITY)
    }

    public class Builder internal constructor() {

        @JvmSynthetic
        internal lateinit var code: String

        @JvmSynthetic
        internal lateinit var reposeType: ResponseType

        @JvmSynthetic
        internal val details: MutableList<ResponseData> = ArrayList(DEFAULT_CONTAINER_CAPACITY)

        @JvmSynthetic
        internal lateinit var message: LocalizableMessageView

        @JvmSynthetic
        internal val data: MutableMap<String, Any> = HashMap(DEFAULT_CONTAINER_CAPACITY)

        public fun code(value: String): Builder = apply { this.code = value }

        public fun reposeType(value: ResponseType): Builder = apply { this.reposeType = value }

        public fun message(value: LocalizableMessage): Builder = apply { this.message = value.asView() }

        public fun message(value: LocalizableMessageView): Builder = apply { this.message = value }

        public fun details(values: Iterable<ResponseData>): Builder = apply {
            this.details.apply {
                clear()
                addAll(values)
            }
        }

        public fun data(value: Map<String, Any>): Builder = apply { this.data.apply { clear(); putAll(value) } }
    }
}
