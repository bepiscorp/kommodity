package tyrell.callisto.signal.service

import tyrell.callisto.base.definition.LibraryApi
import tyrell.callisto.signal.model.Signal
import tyrell.callisto.signal.model.SignalDefinition
import tyrell.callisto.signal.model.SignalSubscription

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public interface SignalSubscriptionService {

    public fun <S : Signal> subscribe(signalDefinition: SignalDefinition): SignalSubscription<S>

    public fun disposeSubscription(subscription: SignalSubscription<*>)
}
