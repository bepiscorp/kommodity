package tyrell.callisto.base.serialization

import com.fasterxml.jackson.databind.DeserializationContext
import tyrell.callisto.base.definition.DelicateLibraryApi
import tyrell.callisto.base.definition.ExperimentalLibraryApi

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@ExperimentalLibraryApi @DelicateLibraryApi
public abstract class ExternalValueProcessorBase public constructor(prefix: String) : ExternalValueProcessor {

    private val prefixRegex = "^$prefix.*://.*".toRegex()

    private val optionalPrefixRegex = "^$prefix.*\\?://.*".toRegex()

    override fun supports(value: String): Boolean = value.matches(prefixRegex)

    final override fun process(
        value: String,
        context: DeserializationContext,
        additionalConfigValues: Map<String, Any>,
    ): Any? {
        val isMandatory: Boolean = !value.matches(optionalPrefixRegex)
        return process(value, isMandatory, context, additionalConfigValues)
    }

    /**
     * Strips out the prefix to get resource path.
     *
     * @param value prefixed value
     *
     * @return resource path
     */
    protected fun getResourcePath(value: String): String = value.substring(value.indexOf("://") + 3)
}
