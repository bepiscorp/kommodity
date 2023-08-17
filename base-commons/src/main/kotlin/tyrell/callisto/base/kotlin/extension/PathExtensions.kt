@file:Suppress("NOTHING_TO_INLINE")

package tyrell.callisto.base.kotlin.extension

import tyrell.callisto.base.definition.ExperimentalLibraryApi
import tyrell.callisto.base.definition.LibraryApi
import java.nio.file.Path

/**
 * Returns whether this path does not contain any names.
 *
 * The implementation is equivalent for this path to:
 * ```
 * return nameCount == 0
 * ```
 *
 * @return `true` if [Path.getNameCount] equals zero, `false` otherwise
 *
 * @author Mikhail Gostev
 * @since 0.0.1
 * @see Path.getNameCount
 */
@LibraryApi
public inline fun Path.isEmpty(): Boolean = nameCount == 0

/**
 * Returns whether this path contains any names.
 *
 * The implementation is equivalent for this path to:
 * ```
 * return nameCount > 0
 * ```
 *
 * @return `true` if [Path.getNameCount] bigger than zero, `false` otherwise
 *
 * @author Mikhail Gostev
 * @since 0.0.1
 * @see Path.getNameCount
 */
@LibraryApi
public inline fun Path.isNotEmpty(): Boolean = nameCount > 0

/**
 * Dynamic property that returns a name 0-th element of this path as a [Path] object.
 * Returns `null` if this path has zero name elements.
 *
 * The implementation is equivalent for this path to:
 * ```
 * return if (nameCount > 0) getName(0) else null
 * ```
 *
 * @author Mikhail Gostev
 * @since 0.0.1
 * @see Path.getName
 */
@ExperimentalLibraryApi
public val Path.startName: Path?
    get() = if (isNotEmpty()) getName(0) else null

/**
 * Returns whether [Path.startName] is not blank.
 *
 * @return `true` if stringified [Path.startName] of this path is not blank, `false` otherwise
 *
 * @author Mikhail Gostev
 * @since 0.0.1
 * @see Path.startName
 */
@ExperimentalLibraryApi
public inline fun Path.hasNonBlankStart(): Boolean = startName?.toString()?.isNotBlank() ?: false

/**
 * Returns whether [Path.startName] is blank.
 *
 * @return `true` if stringified [Path.startName] of this path is blank, `false` otherwise
 *
 * @author Mikhail Gostev
 * @since 0.0.1
 * @see Path.startName
 */
@ExperimentalLibraryApi
public inline fun Path.hasBlankStart(): Boolean = !hasNonBlankStart()

/**
 * Delete this file with all its children.
 * Note that if this operation fails then partial deletion may have taken place.
 *
 * The implementation is equivalent for this path to:
 * ```
 * return toFile().deleteRecursively()
 * ```
 *
 * @return `true` if the file or directory is successfully deleted, `false` otherwise.
 * @author Mikhail Gostev
 * @since 0.0.1
 * @see kotlin.io.deleteRecursively
 */
@ExperimentalLibraryApi
public inline fun Path.deleteRecursively(): Boolean = toFile().deleteRecursively()
