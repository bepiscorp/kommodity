package tyrell.callisto.signal.unix.model

import tyrell.callisto.base.definition.LibraryApi
import tyrell.callisto.signal.model.AbstractSignalDefinition

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public class UnixSignalDefinition(public val code: UnixSignalCode) : AbstractSignalDefinition(UnixSignal.TYPE) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UnixSignalDefinition) return false

        if (code != other.code) return false

        return true
    }

    override fun hashCode(): Int = code.hashCode()

    override fun toString(): String = "UnixSignalDefinition(code=$code)"
}
