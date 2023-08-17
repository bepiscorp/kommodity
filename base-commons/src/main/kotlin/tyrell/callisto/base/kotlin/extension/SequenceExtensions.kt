package tyrell.callisto.base.kotlin.extension

import tyrell.callisto.base.definition.ExperimentalLibraryApi

/**
 * Runs the given [closure] on each element.
 *
 * Similar to [forEach] method but allows performing [run] action on each element.
 * Example:
 * ```
 * sequenceOf(25.0, -25.0, 0.0)
 *     .forEachRunning { print("$sign ") } // Prints: 1.0 -1.0 0.0 | Returns: kotlin.Unit
 * ```
 *
 * The implementation is equivalent to:
 * ```
 * sequence.onEach {
 *     it.run {
 *         ...
 *     }
 * }
 * ```
 *
 * @return the list which function was run on
 *
 * @author Mikhail Gostev
 * @since 0.1.0
 */
@ExperimentalLibraryApi
public inline fun <T> Sequence<T>.forEachRunning(
    crossinline closure: T.() -> Unit,
): Unit = forEach { closure(it) }

/**
 * Runs the given [closure] on each element.
 * Returns the sequence itself afterwards.
 *
 * Similar to [onEach] method but allows performing [run] action on each element.
 * Example:
 * ```
 * sequenceOf(25.0, -25.0, 0.0)
 *     .onEachRunning { print("$sign ") } // Prints: 1.0 -1.0 0.0 | Returns: [25.0, -25.0, 0.0]
 * ```
 *
 * The implementation is equivalent to:
 * ```
 * sequence.onEach {
 *     it.run {
 *         ...
 *     }
 * }
 * ```
 *
 * @return the list which function was run on
 *
 * @author Mikhail Gostev
 * @since 0.1.0
 */
@ExperimentalLibraryApi
public inline fun <T> Sequence<T>.onEachRunning(
    crossinline closure: T.() -> Unit,
): Sequence<T> = onEach { closure(it) }

/**
 * Returns a list containing the results of
 * applying the given [closure] function to each element in the original collection.
 *
 * Similar to [map] method but allows performing [run] transformation on each element.
 * Example:
 * ```
 * sequenceOf(1.0, 2.0, 3.0)
 *     .mapping { pow(2) } // [1.0, 4.0, 9.0]
 * ```
 *
 * The implementation is equivalent to:
 * ```
 * sequence.map {
 *     it.run {
 *         ...
 *     }
 * }
 * ```
 *
 * @return the list with elements mapped by [closure]
 *
 * @author Mikhail Gostev
 * @since 0.1.0
 */
@ExperimentalLibraryApi
@Suppress("NOTHING_TO_INLINE")
public inline fun <T, R> Sequence<T>.mapping(
    noinline closure: T.() -> R,
): Sequence<R> = map { closure(it) }

/**
 * Returns a list containing the elements yielded from results of
 * applying the given [closure] function to each element in the original collection.
 *
 * Similar to [flatMap] method but allows performing [run] transformation on each element.
 * Example:
 * ```
 * sequenceOf(1.0, 2.0, 3.0)
 *     .flatMapping { sequenceOf(pow(1), pow(2)) } // [1.0, 1.0, 2.0, 4.0, 3.0, 9.0]
 * ```
 *
 * The implementation is equivalent to:
 * ```
 * sequence.flatMap {
 *     it.run {
 *         ...
 *     }
 * }
 * ```
 *
 * @return the list with elements yielded by [closure]
 *
 * @author Mikhail Gostev
 * @since 0.1.0
 */
@ExperimentalLibraryApi
public inline fun <T, R> Sequence<T>.flatMapping(
    crossinline closure: T.() -> Sequence<R>,
): Sequence<R> = flatMap { closure(it) }
