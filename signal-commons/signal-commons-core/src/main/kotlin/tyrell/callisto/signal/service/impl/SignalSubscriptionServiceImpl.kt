package tyrell.callisto.signal.service.impl

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import mu.KLogger
import org.springframework.beans.factory.DisposableBean
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import tyrell.callisto.base.definition.InternalLibraryApi
import tyrell.callisto.base.kotlin.dsl.uncheckedCast
import tyrell.callisto.base.kotlin.properties.LoggingDelegates
import tyrell.callisto.signal.SignalManager
import tyrell.callisto.signal.model.BaseSignalSubscription
import tyrell.callisto.signal.model.CompositeSignalSubscription
import tyrell.callisto.signal.model.DomainSignal
import tyrell.callisto.signal.model.DomainSignalHandler
import tyrell.callisto.signal.model.Signal
import tyrell.callisto.signal.model.SignalDefinition
import tyrell.callisto.signal.model.SignalFactoryContext
import tyrell.callisto.signal.model.SignalSubscription
import tyrell.callisto.signal.service.SignalSubscriptionService
import java.time.Clock
import java.time.LocalDateTime
import java.util.concurrent.ConcurrentHashMap

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@Suppress("unused")
@InternalLibraryApi
@Service("signals.subscriptionService")
public class SignalSubscriptionServiceImpl : SignalSubscriptionService, InitializingBean, DisposableBean {

    protected val logger: KLogger by LoggingDelegates.instanceLogger()

    // TODO: Remove hardcode
    internal val clock: Clock = Clock.systemDefaultZone()

    @set:[Lazy Autowired]
    internal lateinit var signalManagers: List<RawSignalManager>

    private lateinit var coroutineDispatcher: ExecutorCoroutineDispatcher

    private lateinit var coroutineScope: CoroutineScope

    private val subscriptionBySignalDefinition: MutableMap<SignalDefinition, BaseSignalSubscription<Signal>> =
        ConcurrentHashMap()

    override fun afterPropertiesSet() {
        this.coroutineDispatcher = newSingleThreadContext("SignalSubscriptionService.SignalHandlerDispatcher")
        this.coroutineScope = CoroutineScope(coroutineDispatcher + SupervisorJob())
    }

    override fun destroy() {
        this.coroutineDispatcher.close()
        this.coroutineScope.cancel()
    }

    override fun <S : Signal> subscribe(signalDefinition: SignalDefinition): SignalSubscription<S> =
        subscriptionBySignalDefinition.computeIfAbsent(signalDefinition) {
            val signalManager = resolveManagerBySignalDefinition(signalDefinition)
            val signalsFlow: MutableSharedFlow<Signal> = MutableSharedFlow()
            val signalHandler: DomainSignalHandler = createDomainSignalHandler(signalsFlow)

            signalManager.registerSignalHandler(signalDefinition, signalHandler)
            logger.debug { "Created signal subscription for [$signalDefinition]" }

            BaseSignalSubscription(signalDefinition, signalsFlow)
        }.let(::uncheckedCast)

    override fun disposeSubscription(subscription: SignalSubscription<Signal>) {
        logger.entry(subscription)

        val signalDefinitions: Set<SignalDefinition> = when (subscription) {
            is BaseSignalSubscription -> setOf(subscription.signalDefinition)
            is CompositeSignalSubscription -> subscription.subscriptions.map { it.signalDefinition }.toSet()
        }

        for (signalDefinition in signalDefinitions) {
            subscriptionBySignalDefinition.computeIfPresent(signalDefinition) compute@{ _, _ ->
                logger.trace { "Removing signal subscription for [$signalDefinition]" }

                val signalManager = resolveManagerBySignalDefinition(signalDefinition)
                if (signalManager.unregisterSignalHandlers(signalDefinition)) {
                    logger.debug { "Unregistered signal handler for [$signalDefinition]" }
                }

                return@compute null
            }
        }
    }

    private fun createDomainSignalHandler(flow: MutableSharedFlow<Signal>): DomainSignalHandler =
        { domainSignal: DomainSignal ->
            val currentTimestamp: LocalDateTime = LocalDateTime.now(clock)

            val signalManager = resolveManagerByDomainSignal(domainSignal)
            val context: SignalFactoryContext<*> = SignalFactoryContext(domainSignal, currentTimestamp)

            val newSignal: Signal = signalManager.buildSignal(context)

            // TODO: It is possible bottleneck as singlethreaded context is used
            coroutineScope.launch { flow.emit(newSignal) }
        }

    private fun resolveManagerByDomainSignal(domainSignal: DomainSignal): BaseSignalManager =
        signalManagers
            .firstOrNull { it.domainSignalClass == domainSignal.javaClass }
            .let(::uncheckedCast)
            ?: error("Could not resolve signal factory for domain signal [$domainSignal]")

    private fun resolveManagerBySignalDefinition(signalDefinition: SignalDefinition): BaseSignalManager =
        signalManagers
            .firstOrNull { it.signalType == signalDefinition.type }
            .let(::uncheckedCast)
            ?: error("Could not resolve signal factory for signal typ name [$${signalDefinition.type}]")
}

private typealias BaseSignalManager = SignalManager<SignalDefinition, Signal, DomainSignal>
private typealias RawSignalManager = SignalManager<*, *, *>
