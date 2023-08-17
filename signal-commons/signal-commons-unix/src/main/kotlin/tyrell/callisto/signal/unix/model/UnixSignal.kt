package tyrell.callisto.signal.unix.model

import tyrell.callisto.base.definition.LibraryApi
import tyrell.callisto.signal.model.AbstractSignal
import java.time.LocalDateTime

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public class UnixSignal(
    code: UnixSignalCode,
    timestamp: LocalDateTime,
) : AbstractSignal(TYPE, timestamp, createPayload(code)) {

    public val code: UnixSignalCode = code

    override fun toString(): String = "UnixSignal(code='$code', timestamp=$timestamp)"

    private object PayloadKeys {

        const val CODE: String = "code"
    }

    public companion object {

        internal const val TYPE: String = "UNIX"

        public fun createPayload(code: UnixSignalCode): Map<String, Any> = mapOf(PayloadKeys.CODE to code)
    }
}

internal typealias JvmSignal = sun.misc.Signal
internal typealias JvmSignalHandler = sun.misc.SignalHandler
