package tyrell.callisto.base.kotlin.extension

import tyrell.callisto.base.definition.ExperimentalLibraryApi

/**
 * Performs the given [action] on each element.
 *
 * Performant analogue of [kotlin.collections.forEach] method.
 *
 * The implementation is equivalent to:
 * ```
 * for (index in indices) {
 *     val element = get(index)
 *     block(element)
 * }
 * ```
 *
 * @param [action] function that takes an element and performs the action on it.
 *
 * @author Mikhail Gostev
 * @since 0.1.0
 */
@ExperimentalLibraryApi
public inline fun <T> Array<out T>.foreach(
    crossinline action: (element: T) -> Unit,
) {
    for (index in indices) {
        val element = get(index)
        action(element)
    }
}

/**
 * Performs the given [action] on each element, providing sequential index with the element.
 *
 * Performant analogue of [kotlin.collections.forEachIndexed] method.
 *
 * The implementation is equivalent to:
 * ```
 * for (index in indices) {
 *     val element = get(index)
 *     block(index, element)
 * }
 * ```
 *
 * @param [action] function that takes the index of an element and the element itself
 * and performs the action on the element.
 *
 * @author Mikhail Gostev
 * @since 0.1.0
 */
@ExperimentalLibraryApi
public inline fun <T> Array<out T>.foreachIndexed(
    crossinline action: (index: Int, element: T) -> Unit,
) {
    for (index in indices) {
        val element = get(index)
        action(index, element)
    }
}

/**
 * Performs the given [action] on each element and returns the array itself afterwards.
 *
 * Performant analogue of [kotlin.collections.onEach] method.
 *
 * The implementation is equivalent to:
 * ```
 * for (index in list.indices) {
 *     val element = list.get(index)
 *     block(element)
 * }
 * return list
 * ```
 *
 * @param [action] function that takes an element and performs the action on it.
 *
 * @author Mikhail Gostev
 * @since 0.1.0
 */
@ExperimentalLibraryApi
public inline fun <T> Array<out T>.onEach(
    crossinline action: (element: T) -> Unit,
): Array<out T> = apply {
    for (index in indices) {
        val element = get(index)
        action(element)
    }
}

/**
 * Performs the given [action] on each element, providing sequential index with the element,
 * and returns the array itself afterwards.
 *
 * Performant analogue of [kotlin.collections.onEachIndexed] method.
 *
 * The implementation is equivalent to:
 * ```
 * for (index in list.indices) {
 *     val element = list.get(index)
 *     block(index, element)
 * }
 * return list
 * ```
 *
 * @param [action] function that takes the index of an element and the element itself
 * and performs the action on the element.
 *
 * @author Mikhail Gostev
 * @since 0.1.0
 */
@ExperimentalLibraryApi
public inline fun <T> Array<out T>.onEachIndexed(
    crossinline action: (index: Int, element: T) -> Unit,
): Array<out T> = apply {
    for (index in indices) {
        val element = get(index)
        action(index, element)
    }
}
