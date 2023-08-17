package tyrell.callisto.base.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.BeanProperty
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.deser.ContextualDeserializer
import tyrell.callisto.base.definition.InternalLibraryApi
import tyrell.callisto.base.kotlin.dsl.uncheckedCast

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@InternalLibraryApi
public class ExternalValueDeserializer : JsonDeserializer<Any>, ContextualDeserializer {

    private val processors: Set<ExternalValueProcessor>?

    private val additionalValues: Map<String, Any>?

    public constructor() {
        this.processors = null
        this.additionalValues = null
    }

    public constructor(processors: Set<ExternalValueProcessor>) {
        this.processors = processors
        this.additionalValues = null
    }

    public constructor(
        processors: Set<ExternalValueProcessor>,
        additionalValues: Map<String, Any>,
    ) {
        this.processors = processors
        this.additionalValues = additionalValues
    }

    override fun deserialize(parser: JsonParser, context: DeserializationContext): Any? {
        if (parser.currentToken != JsonToken.VALUE_STRING) {
            throw context.wrongTokenException(
                parser, Any::class.java,
                JsonToken.VALUE_STRING, "Unable to process value with ExternalJsonDeserializer",
            )
        }
        val value: String = parser.text

        val processor: ExternalValueProcessor = getValueProcessors(context)
            .firstOrNull { it.supports(value) }
            ?: return value // Returning a regular string if it has not found matching processor

        try {
            val processedValue: Any? = processor.process(value, context, getAdditionalValues(context))
            return processedValue
        } catch (ex: Exception) {
            throw context.weirdStringException(value, Any::class.java, ex.message)
        }
    }

    override fun createContextual(
        context: DeserializationContext,
        property: BeanProperty?,
    ): ExternalValueDeserializer = this

    private fun getValueProcessors(context: DeserializationContext): Set<ExternalValueProcessor> {
        if (this.processors != null) {
            return this.processors
        }

        val processors: Set<ExternalValueProcessor> = context.getAttribute(VALUE_PROCESSORS_SET_ATTRIBUTE)
            .let(::uncheckedCast)
            ?: throw IllegalStateException(
                "Set of JsonValueProcessor was not provided" +
                    " neither for initialization of ExternalJsonDeserializer nor for deserialization",
            )

        return processors
    }

    private fun getAdditionalValues(context: DeserializationContext): Map<String, Any> =
        this.additionalValues ?: context.getAttribute(ADDITIONAL_VALUES_ATTRIBUTE).let(::uncheckedCast) ?: emptyMap()

    public companion object {

        @JvmField
        public val VALUE_PROCESSORS_SET_ATTRIBUTE: String =
            "__${ExternalValueDeserializer::class.simpleName}#processorsSet"

        @JvmField
        public val ADDITIONAL_VALUES_ATTRIBUTE: String =
            "__${ExternalValueDeserializer::class.simpleName}#additionalValues"
    }
}
