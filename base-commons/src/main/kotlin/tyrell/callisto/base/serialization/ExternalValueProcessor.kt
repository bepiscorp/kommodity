package tyrell.callisto.base.serialization

import com.fasterxml.jackson.databind.DeserializationContext
import tyrell.callisto.base.definition.ExperimentalLibraryApi

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@ExperimentalLibraryApi
public interface ExternalValueProcessor {

    /**
     * Checks whether this processor can process the given value.
     *
     * @param value value to process
     *
     * @return `true` if this processor can process the given value, `false` otherwise
     */
    public fun supports(value: String): Boolean

    /**
     * Processes the given value and returns the result of the processing.
     *
     * @param value value to process
     * @param context deserialization context
     *
     * @return result of the processing
     */
    public fun process(
        value: String,
        context: DeserializationContext,
        additionalConfigValues: Map<String, Any>,
    ): Any?

    /**
     * Processes the given value and returns the result of the processing.
     *
     * @param value value to process
     * @param mandatory determines if the value is mandatory
     * @param context deserialization context
     * @param additionalConfigValues additional values for contextual processing
     *
     * @return result of the processing
     */
    public fun process(
        value: String,
        mandatory: Boolean,
        context: DeserializationContext,
        additionalConfigValues: Map<String, Any>,
    ): Any?
}
