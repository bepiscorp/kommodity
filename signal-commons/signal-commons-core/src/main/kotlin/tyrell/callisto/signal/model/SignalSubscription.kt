package tyrell.callisto.signal.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flattenMerge
import tyrell.callisto.base.definition.ExperimentalLibraryApi
import tyrell.callisto.base.definition.LibraryApi

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public sealed interface SignalSubscription<out S : Signal> {

    public fun asSignalsFlow(): Flow<S>
}

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@ExperimentalLibraryApi
public fun <S : Signal> SignalSubscription<S>.mergeWith(
    vararg subscriptions: SignalSubscription<S>,
): SignalSubscription<S> = sequence {
    yield(this@mergeWith)
    subscriptions.forEach { subscription ->
        if (subscription is CompositeSignalSubscription<S>) {
            yieldAll(subscription.subscriptions)
        } else {
            yield(subscription)
        }
    }
}.asIterable().let(::CompositeSignalSubscription)

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@ExperimentalLibraryApi
public fun <S : Signal> mergeSubscriptions(
    vararg subscriptions: SignalSubscription<S>,
): SignalSubscription<S> = CompositeSignalSubscription(subscriptions.asIterable())

internal class BaseSignalSubscription<S : Signal>(

    internal val signalDefinition: SignalDefinition,

    internal val signalsFlow: MutableSharedFlow<S>,
) : SignalSubscription<S> {

    override fun asSignalsFlow(): Flow<S> = signalsFlow.asSharedFlow()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BaseSignalSubscription<*>) return false

        if (signalDefinition != other.signalDefinition) return false

        return true
    }

    override fun hashCode(): Int = signalDefinition.hashCode()
}

internal class CompositeSignalSubscription<S : Signal> private constructor(
    internal val subscriptions: Set<BaseSignalSubscription<S>>,
) : SignalSubscription<S> {

    constructor(subscriptions: Iterable<SignalSubscription<S>>) :
        this(createSubscriptionsSet(subscriptions))

    private val mergedFlow: Flow<S> by lazy {
        subscriptions.map { it.asSignalsFlow() }
            .asFlow()
            .flattenMerge(subscriptions.size)
    }

    override fun asSignalsFlow(): Flow<S> = mergedFlow

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CompositeSignalSubscription<*>) return false

        if (subscriptions != other.subscriptions) return false

        return true
    }

    override fun hashCode(): Int = subscriptions.hashCode()
}

private fun <S : Signal> createSubscriptionsSet(
    subscriptions: Iterable<SignalSubscription<S>>,
): Set<BaseSignalSubscription<S>> =
    subscriptions.asSequence()
        .flatMap {
            when (it) {
                is BaseSignalSubscription<S> -> sequenceOf(it)
                is CompositeSignalSubscription<S> -> it.subscriptions.asSequence()
            }
        }
        .toSet()
