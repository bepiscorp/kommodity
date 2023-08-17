package tyrell.callisto.base.kotlin.extension

@PublishedApi
internal const val SPACE_CHAR: Char = ' '

/**
 * Align given [CharSequence] inside box of given length [length] using padding character [padChar].
 *
 * @author Mikhail Gostev
 * @since 0.1.0
 *
 * @see CharSequence.padStart
 * @see CharSequence.padEnd
 */
public fun CharSequence.padCenter(length: Int, padChar: Char = SPACE_CHAR): CharSequence {
    require(length >= 0) { "Length must be non-negative, but was [$length]." }
    return when {
        this.isEmpty() -> padChar.repeat(length)
        (length <= this.length) -> this
        else -> StringBuilder(length).apply {
            val content = this@padCenter
            val charsToAppend = (length - content.length)
            repeat(charsToAppend / 2) { append(padChar) } // Left padding
            append(content) // Text itself
            repeat((charsToAppend / 2) + (charsToAppend % 2)) { append(padChar) } // Right padding
        }
    }
}

/**
 * Align given [String] inside box of given length [length] using padding character [padChar].
 *
 * @author Mikhail Gostev
 * @since 0.1.0
 *
 * @see String.padStart
 * @see String.padEnd
 */
public fun String.padCenter(length: Int, padChar: Char = SPACE_CHAR): String =
    (this as CharSequence).padCenter(length, padChar).toString()

/**
 * Create a string of [SPACE_CHAR] chars with length [n].
 *
 * @author Mikhail Gostev
 * @since 0.1.0
 *
 * @see Char.repeat
 */
@Suppress("NOTHING_TO_INLINE")
public inline fun spaces(n: Int): String = SPACE_CHAR.repeat(n)

/**
 * Create a string of given [Char] with length [n].
 *
 * @author Mikhail Gostev
 * @since 0.1.0
 *
 * @see String.repeat
 */
public fun Char.repeat(n: Int): String {
    require(n >= 0) { "Count 'n' must be non-negative, but was [$n]." }
    return when (n) {
        0 -> ""
        1 -> this.toString()
        else -> buildString(capacity = n) { repeat(n) { append(this@repeat) } }
    }
}
