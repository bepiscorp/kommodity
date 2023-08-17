package tyrell.callisto.signal

import tyrell.callisto.base.definition.LibraryApi
import tyrell.callisto.signal.model.DomainSignal
import tyrell.callisto.signal.model.Signal
import tyrell.callisto.signal.model.SignalDefinition
import tyrell.callisto.signal.model.SignalFactoryContext

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public interface SignalManager<in SD : SignalDefinition, S : Signal, DS : DomainSignal> {

    public val signalType: String

    public val signalClass: Class<S>

    public val domainSignalClass: Class<DS>

    public fun buildSignal(context: SignalFactoryContext<DS>): S

    public fun registerSignalHandler(signalDefinition: SD, handler: (DomainSignal) -> Unit)

    public fun unregisterSignalHandlers(signalDefinition: SD): Boolean
}
