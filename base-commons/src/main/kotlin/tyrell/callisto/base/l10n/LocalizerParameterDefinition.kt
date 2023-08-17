package tyrell.callisto.base.l10n

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import tyrell.callisto.base.definition.InternalLibraryApi
import tyrell.callisto.base.type.ParameterDefinition

/**
 * [LocalizerParameterDefinition] stores parameter name and index of the localizing message
 *
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@InternalLibraryApi
public class LocalizerParameterDefinition @JsonCreator constructor(

    override val code: String,

    public val index: Int,
) : ParameterDefinition {

    @delegate:JsonIgnore
    public val parameterPlaceholder: String by lazy { "\\$\\{$code(,.*)?\\}" }

    @delegate:JsonIgnore
    public val indexPlaceholder: String by lazy { "{$index$1}" }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LocalizerParameterDefinition) return false

        if (code != other.code) return false

        return true
    }

    override fun hashCode(): Int = code.hashCode()
}
