package tyrell.callisto.base.kotlin.extension

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import tyrell.callisto.base.definition.ExperimentalLibraryApi

/**
 * Performs the given [action] on each element.
 *
 * The implementation is equivalent to:
 * ```
 * coroutineScope {
 *     map { launch { action(it) } }
 *         .joinAll()
 * }
 * ```
 *
 * @author Mikhail Gostev
 * @since 0.1.0
 */
@ExperimentalLibraryApi
public suspend inline fun <T> Iterable<T>.forEachAsync(
    crossinline action: suspend (element: T) -> Unit,
): Unit = coroutineScope {
    map { launch { action(it) } }
        .joinAll()
}

/**
 * Returns a list containing the results of applying the given [transform] function
 * to each element in the original collection.
 *
 * The implementation is equivalent to:
 * ```
 * coroutineScope {
 *     map { async { transform(it) } }
 *         .awaitAll()
 * }
 * ```
 *
 * @author Mikhail Gostev
 * @since 0.1.0
 */
@ExperimentalLibraryApi
public suspend inline fun <A, B> Iterable<A>.mapAsync(
    crossinline transform: suspend (element: A) -> B,
): List<B> = coroutineScope {
    map { async { transform(it) } }
        .awaitAll()
}

/**
 * Returns a list containing only the non-null results of applying the given [transform] function
 * to each element in the original collection.
 *
 * The implementation is equivalent to:
 * ```
 * coroutineScope {
 *     map { async { transform(it) } }
 *         .awaitAll()
 *         .filterNotNull()
 * }
 * ```
 *
 * @author Mikhail Gostev
 * @since 0.1.0
 */
@ExperimentalLibraryApi
public suspend inline fun <A, B> Iterable<A>.mapNotNullAsync(
    crossinline transform: suspend (element: A) -> B?,
): List<B> = coroutineScope {
    map { async { transform(it) } }
        .awaitAll()
        .filterNotNull()
}
