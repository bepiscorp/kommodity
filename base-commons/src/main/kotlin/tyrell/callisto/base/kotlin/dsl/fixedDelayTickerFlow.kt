package tyrell.callisto.base.kotlin.dsl

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import tyrell.callisto.base.definition.DelicateLibraryApi
import tyrell.callisto.base.definition.LibraryApi
import kotlin.time.Duration

/**
 * Creates flow signals ([Unit] object) periodically. Periodicity is specified with [period].
 * Caller can define [initialDelay] for delay of the first signal (default value is [Duration.ZERO])
 *
 * @param period periodicity of signals
 * @param initialDelay delay of the first signal
 * @return flow of signals
 *
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi @DelicateLibraryApi
public inline fun fixedDelayTickerFlow(
    period: Duration,
    initialDelay: Duration = Duration.ZERO,
): Flow<Unit> = flow {
    delay(initialDelay.inWholeMilliseconds)

    while (true) {
        emit(Unit)
        delay(period.inWholeMilliseconds)
    }
}
