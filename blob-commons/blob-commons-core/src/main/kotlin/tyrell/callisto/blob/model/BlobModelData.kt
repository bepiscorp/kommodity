package tyrell.callisto.blob.model

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonAnySetter
import com.fasterxml.jackson.annotation.JsonInclude
import tyrell.callisto.base.definition.InternalLibraryApi
import java.io.Serializable

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@InternalLibraryApi
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class BlobModelData : Serializable {

    private val unknownElements = HashMap<String, Any>()

    @JsonAnySetter
    internal fun setUnknownField(key: String, value: Any) {
        unknownElements[key] = value
    }

    @Suppress("unused")
    @JsonAnyGetter
    internal fun getUnknownElements(): Map<String, Any>? = unknownElements.takeIf { it.isNotEmpty() }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BlobModelData

        if (unknownElements != other.unknownElements) return false

        return true
    }

    override fun hashCode(): Int = unknownElements.hashCode()
}
