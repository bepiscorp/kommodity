package tyrell.callisto.base.l10n

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import tyrell.callisto.base.definition.LibraryApi
import java.text.ChoiceFormat

/**
 * The [TextTemplate] class contains all data required for localizing and formatting message with [Localizer.localize].
 *
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public class TextTemplate {

    /**
     * Substitution text
     */
    public val text: String

    /**
     * Arguments specific format [Map].
     * `Key` is argument index as specified in substitution text,
     * `value` is [ChoiceFormat] describing value specific format text.
     */
    @get:JsonIgnore
    public val args: Map<Int, ChoiceFormat>

    /**
     * Constructs [TextTemplate] out of substitution text and value specific format for arguments.
     * Value specific format may cover only some arguments, or even be empty.
     *
     * @param text substitution text specific to locale
     * @param args arguments specific format [Map].
     * `Key` is argument index as specified in substitution text,
     * `value` is [ChoiceFormat] describing value specific format text.
     */
    public constructor(text: String, args: Map<Int, ChoiceFormat> = emptyMap()) {
        require(text.isNotEmpty()) { "Property 'text' cannot be null or empty" }

        this.text = text
        this.args = args
    }

    /**
     * Constructs [TextTemplate] out of substitution text.
     *
     * *Note*: It can be used while deserialization from simplified (without arguments specific format) JSON.
     *
     * @param text substitution text specific to locale
     */
    @JsonCreator
    public constructor(text: String) : this(text, emptyMap())

    /**
     * Constructs [TextTemplate] out of substitution text and value specific format for arguments.
     *
     * *Note*: It is used while deserialization from JSON.
     *
     * @param text substitution text specific to locale
     * @param args arguments specific format [Map].
     * Each list element contains index ([ArgumentFormat.index]) of argument
     * as specified in substitution text, as well as value specific format text ([ArgumentFormat.formats]).
     */
    @JsonCreator
    @Suppress("unused")
    internal constructor(
        @JsonProperty("text") text: String,
        @JsonProperty("args") args: List<ArgumentFormat>,
    ) {
        require(text.isNotEmpty()) { "Property 'text' cannot be null or empty" }

        this.text = text
        this.args = computeArgs(args)
    }

    @JsonProperty("args")
    @Suppress("unused")
    private fun computeJsonArgs(args: Map<Int, ChoiceFormat>): List<Any> =
        args.map { (key, value) ->
            val limitPairs = mutableListOf<LimitPair>()

            val formats: Array<Any> = value.formats
            val limits: DoubleArray = value.limits

            val formatLimitSequence = formats.asSequence().zip(limits.asSequence())
            for ((format, limit) in formatLimitSequence) {
                limitPairs += LimitPair(limit, format as String)
            }

            ArgumentFormat(key, limitPairs)
        }

    private fun computeArgs(jsonArgs: List<ArgumentFormat>): Map<Int, ChoiceFormat> =
        jsonArgs.asSequence().map { argumentFormat ->
            val formatPairs: List<LimitPair> = argumentFormat.formats

            val (limits: List<Double>, formats: List<String?>) = formatPairs.asSequence()
                .map { it.limit to it.text }
                .unzip()

            val argumentIndex = argumentFormat.index
            val format = ChoiceFormat(limits.toTypedArray().toDoubleArray(), formats.toTypedArray())
            Pair(argumentIndex, format)
        }.toMap()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TextTemplate) return false

        if (text != other.text) return false
        if (args != other.args) return false

        return true
    }

    override fun hashCode(): Int {
        var result = text.hashCode()
        result = 31 * result + args.hashCode()
        return result
    }

    override fun toString(): String = "TextTemplate(text='$text', args=$args)"
}
