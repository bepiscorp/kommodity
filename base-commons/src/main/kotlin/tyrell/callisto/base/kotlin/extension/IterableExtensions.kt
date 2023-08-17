package tyrell.callisto.base.kotlin.extension

import tyrell.callisto.base.definition.ExperimentalLibraryApi

/**
 * Runs the given [closure] on each element.
 *
 * Similar to [forEach] method but allows performing [run] action on each element.
 * Example:
 * ```
 * listOf(25.0, -25.0, 0.0)
 *     .forEachRunning { print("$sign ") } // Prints: 1.0 -1.0 0.0 | Returns: kotlin.Unit
 * ```
 *
 * The implementation is equivalent to:
 * ```
 * iterable.forEachRunning {
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
public inline fun <T, C : Iterable<T>> C.forEachRunning(
    crossinline closure: T.() -> Unit,
): Unit = forEach(closure)

/**
 * Runs the given [closure] on each element.
 * Returns the collection itself afterwards.
 *
 * Similar to [onEach] method but allows performing [run] action on each element.
 * Example:
 * ```
 * listOf(25.0, -25.0, 0.0)
 *     .onEachRunning { print("$sign ") } // Prints: 1.0 -1.0 0.0 | Returns: [25.0, -25.0, 0.0]
 * ```
 *
 * The implementation is equivalent to:
 * ```
 * iterable.onEach {
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
public inline fun <T, C : Iterable<T>> C.onEachRunning(
    crossinline closure: T.() -> Unit,
): C = onEach(closure)

/**
 * Returns a list containing the results of
 * applying the given [closure] function to each element in the original collection.
 *
 * Similar to [map] method but allows performing [run] transformation on each element.
 * Example:
 * ```
 * listOf(1.0, 2.0, 3.0)
 *     .mapping { pow(2) } // [1.0, 4.0, 9.0]
 * ```
 *
 * The implementation is equivalent to:
 * ```
 * iterable.map {
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
public inline fun <T, R> Iterable<T>.mapping(
    crossinline closure: T.() -> R,
): List<R> = map(closure)

/**
 * Returns a list containing the elements yielded from results of
 * applying the given [closure] function to each element in the original collection.
 *
 * Similar to [flatMap] method but allows performing [run] transformation on each element.
 * Example:
 * ```
 * listOf(1.0, 2.0, 3.0)
 *     .flatMapping { listOf(pow(1), pow(2)) } // [1.0, 1.0, 2.0, 4.0, 3.0, 9.0]
 * ```
 *
 * The implementation is equivalent to:
 * ```
 * iterable.flatMap {
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
public inline fun <T, R> Iterable<T>.flatMapping(
    crossinline closure: T.() -> Iterable<R>,
): List<R> = flatMap(closure)
