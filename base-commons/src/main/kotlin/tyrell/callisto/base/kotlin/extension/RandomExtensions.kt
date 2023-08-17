package tyrell.callisto.base.kotlin.extension

import kotlin.random.Random

/**
 * Retrieve random instance of [enumClass] enum class.
 *
 * @author Mikhail Gostev
 * @since 0.1.0
 */
public fun <T : Enum<T>> Random.nextEnum(enumClass: Class<T>): T {
    val values = enumClass.enumConstants
    val index = nextInt(until = values.size)
    return values[index]
}

/**
 * Retrieve random instance of [T] enum class.
 *
 * @author Mikhail Gostev
 * @since 0.1.0
 */
public inline fun <reified T : Enum<T>> Random.nextEnum(): T {
    val values = enumValues<T>()
    val index = nextInt(until = values.size)
    return values[index]
}
