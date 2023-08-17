package tyrell.callisto.signal.unix

import org.springframework.stereotype.Component
import tyrell.callisto.base.definition.InternalLibraryApi
import tyrell.callisto.signal.AbstractSignalManager
import tyrell.callisto.signal.model.DomainSignalHandler
import tyrell.callisto.signal.model.SignalFactoryContext
import tyrell.callisto.signal.unix.model.JvmSignal
import tyrell.callisto.signal.unix.model.UnixDomainSignal
import tyrell.callisto.signal.unix.model.UnixSignal
import tyrell.callisto.signal.unix.model.UnixSignalCode
import tyrell.callisto.signal.unix.model.UnixSignalDefinition
import tyrell.callisto.signal.unix.model.asJvmSignal
import java.util.concurrent.ConcurrentHashMap

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@InternalLibraryApi
@Component("signals.manager.${UnixSignal.TYPE}")
public class UnixSignalManager : AbstractSignalManager<UnixSignalDefinition, UnixSignal, UnixDomainSignal>() {

    private val signalHandlersByCode: MutableMap<UnixSignalCode, List<DomainSignalHandler>> = ConcurrentHashMap()

    override fun buildSignal(context: SignalFactoryContext<UnixDomainSignal>): UnixSignal {
        val domainSignal: JvmSignal = context.domainSignal.value
        val signalCode: UnixSignalCode = UnixSignalCode.fromCode(domainSignal.name)

        return UnixSignal(
            code = signalCode,
            timestamp = context.currentTimestamp,
        )
    }

    override fun registerSignalHandler(signalDefinition: UnixSignalDefinition, handler: DomainSignalHandler) {
        val signalCode: UnixSignalCode = signalDefinition.code

        signalHandlersByCode.compute(signalCode) { _, handlers ->
            (handlers ?: emptyList()).toMutableList() + handler
        }

        JvmSignal.handle(signalCode.asJvmSignal()) { rawDomainSignal: JvmSignal ->
            handler.invoke(UnixDomainSignal(rawDomainSignal))
        }
    }

    override fun unregisterSignalHandlers(signalDefinition: UnixSignalDefinition): Boolean {
        val signalCode: UnixSignalCode = signalDefinition.code

        val hasRemovedHandlers: Boolean = (signalHandlersByCode.remove(signalCode) != null)
        return hasRemovedHandlers
    }
}
