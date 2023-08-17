package tyrell.callisto.base.l10n

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Utility class to deserialize [ChoiceFormat][java.text.ChoiceFormat] from JSON.
 *
 * @author Mikhail Gostev
 * @since 0.0.1
 */
internal data class ArgumentFormat @JsonCreator constructor(

    @get:JsonProperty(value = "index", required = true)
    val index: Int,

    @get:JsonProperty(value = "formats")
    val formats: List<LimitPair>,
) {

    init {
        require(formats.isNotEmpty()) { "Property 'formats' cannot be null or empty" }
    }
}
