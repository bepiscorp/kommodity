package tyrell.callisto.data.typing

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import tyrell.callisto.base.definition.LibraryApi
import tyrell.callisto.data.typing.type.DataType
import java.util.function.Consumer

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public open class BaseParameterDefinition @JsonCreator protected constructor(

    final override val code: String,

    override val dataType: DataType<*>,

    public val mandatory: Boolean,

    public val nullable: Boolean,

    public val defaultValue: Any?,
) : TypedParameterDefinition<Any> {

    @JvmSynthetic
    public fun copy(closure: (BuilderScope.() -> Unit)): BaseParameterDefinition {
        val instance: BaseParameterDefinition = this@BaseParameterDefinition
        val scope = BuilderScope()

        scope.apply {
            code = instance.code
            dataType = instance.dataType
            mandatory = instance.mandatory
            nullable = instance.nullable
            defaultValue = instance.defaultValue

            closure(this)
        }

        return BaseParameterDefinition(
            code = scope.code,
            dataType = scope.dataType,
            mandatory = scope.mandatory,
            nullable = scope.nullable,
            defaultValue = scope.defaultValue,
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BaseParameterDefinition) return false

        if (code != other.code) return false
        if (dataType != other.dataType) return false
        if (mandatory != other.mandatory) return false
        if (nullable != other.nullable) return false
        if (defaultValue != other.defaultValue) return false

        return true
    }

    override fun hashCode(): Int {
        var result = code.hashCode()
        result = 31 * result + dataType.hashCode()
        result = 31 * result + mandatory.hashCode()
        result = 31 * result + nullable.hashCode()
        result = 31 * result + (defaultValue?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String =
        "BaseParameterDefinition(code='$code'," +
            " dataType=$dataType," +
            " mandatory=$mandatory," +
            " nullable=$nullable," +
            " defaultValue=$defaultValue" +
            ")"

    public companion object {

        @JvmStatic
        @JvmSynthetic
        public fun build(closure: BuilderScope.() -> Unit): BaseParameterDefinition {
            val scope = BuilderScope().apply(closure)
            return BaseParameterDefinition(
                code = scope.code,
                dataType = scope.dataType,
                mandatory = scope.mandatory,
                nullable = scope.nullable,
                defaultValue = scope.defaultValue,
            )
        }

        @JvmStatic
        public fun build(consumer: Consumer<Builder>): BaseParameterDefinition {
            val builder = Builder().apply { consumer.accept(this) }
            return BaseParameterDefinition(
                code = builder.code,
                dataType = builder.dataType,
                mandatory = builder.mandatory,
                nullable = builder.nullable,
                defaultValue = builder.defaultValue,
            )
        }
    }

    public open class BuilderScope internal constructor() {

        public open lateinit var code: String

        public open lateinit var dataType: DataType<*>

        public open var mandatory: Boolean = false

        public open var nullable: Boolean = true

        public open var defaultValue: Any? = null
    }

    public open class Builder internal constructor() {

        @JvmSynthetic
        internal open lateinit var code: String

        @JvmSynthetic
        internal open lateinit var dataType: DataType<*>

        @JvmSynthetic
        internal open var mandatory: Boolean = false

        @JvmSynthetic
        internal open var nullable: Boolean = true

        @JvmSynthetic
        internal open var defaultValue: Any? = null

        public fun code(value: String): Builder = apply { this.code = value }

        public fun dataType(value: DataType<*>): Builder = apply { this.dataType = value }

        public fun mandatory(value: Boolean): Builder = apply { this.mandatory = value }

        public fun nullable(value: Boolean): Builder = apply { this.nullable = value }

        public fun defaultValue(value: Any?): Builder = apply { this.defaultValue = value }
    }
}
