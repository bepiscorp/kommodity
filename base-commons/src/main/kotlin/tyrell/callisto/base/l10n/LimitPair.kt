package tyrell.callisto.base.l10n

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

/**
 *
 * Utility class to deserialize [ChoiceFormat][java.text.ChoiceFormat] from JSON.
 *
 * [LimitPair] describes substitution message specific for limit.
 *
 * @author Mikhail Gostev
 * @since 0.0.1
 * @see java.text.ChoiceFormat
 */
internal data class LimitPair @JsonCreator constructor(

    @get:JsonProperty("limit", required = true)
    val limit: Double,

    @get:JsonProperty("text")
    val text: String? = null,
)
