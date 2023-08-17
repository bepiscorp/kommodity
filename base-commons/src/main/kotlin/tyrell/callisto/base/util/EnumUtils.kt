package tyrell.callisto.base.util

import tyrell.callisto.base.definition.ExperimentalLibraryApi
import tyrell.callisto.base.kotlin.dsl.uncheckedCast
import java.util.EnumSet
import java.util.concurrent.ConcurrentHashMap

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@ExperimentalLibraryApi
public object EnumUtils {

    private val cache: MutableMap<Class<*>, Map<String, Enum<*>>> = ConcurrentHashMap()

    /**
     * Returns data type by its string representation. Supports both upper case and camel case variants.
     *
     * @param stringValue the string representation
     * @param elementType the enumeration element type
     * @return the enumeration element
     */
    public fun <E : Enum<E>> readElement(stringValue: String, elementType: Class<E>): E {
        require(stringValue.isNotBlank()) {
            "Illegal value [$stringValue] was provided for enum element reading"
        }
        return getEnumCache(elementType)[stringValue]
            .let(::uncheckedCast)
            ?: throw IllegalArgumentException(
                "Enumeration ${elementType.canonicalName} does not have element $stringValue",
            )
    }

    /**
     * Returns whether enumeration contains element with the given string representation.
     * Supports both upper case and camel case variants.
     *
     * @param stringValue the string representation
     * @param elementClass the enumeration element type
     * @return `true` if matching element exists or string value is `null` or empty; `false` otherwise
     */
    public fun <E : Enum<E>> containsElement(stringValue: String?, elementClass: Class<E>): Boolean =
        !stringValue.isNullOrBlank() && (stringValue in getEnumCache(elementClass))

    private fun <E : Enum<E>> getEnumCache(elementClass: Class<E>): Map<String, Enum<*>> =
        cache.computeIfAbsent(elementClass) {
            EnumSet.allOf(elementClass).asSequence()
                .map { enumValue: E -> enumValue.name to enumValue }
                .flatMap { (name: String, element: E) ->
                    sequenceOf(
                        (name to element),
                        // TODO: Add camelCase name
                    )
                }
                .toMap()
        }
}
