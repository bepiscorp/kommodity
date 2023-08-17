package tyrell.callisto.signal.model

import tyrell.callisto.base.definition.LibraryApi
import java.time.LocalDateTime

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public data class SignalFactoryContext<out DS : DomainSignal>(

    val domainSignal: DS,

    val currentTimestamp: LocalDateTime,
)
