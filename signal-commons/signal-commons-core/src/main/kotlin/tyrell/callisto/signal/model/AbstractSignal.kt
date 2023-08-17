package tyrell.callisto.signal.model

import tyrell.callisto.base.definition.LibraryApi
import java.time.LocalDateTime

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public abstract class AbstractSignal protected constructor(

    override val type: String,

    override val timestamp: LocalDateTime,

    internal val payload: Map<String, Any>,
) : Signal {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AbstractSignal) return false

        if (type != other.type) return false
        if (timestamp != other.timestamp) return false
        if (payload != other.payload) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + timestamp.hashCode()
        result = 31 * result + payload.hashCode()
        return result
    }

    override fun toString(): String = "BaseSignal(type='$type', timestamp=$timestamp, payload=$payload)"
}
