@file:Suppress("NOTHING_TO_INLINE")

package tyrell.callisto.base.kotlin.properties

import tyrell.callisto.base.definition.DelicateLibraryApi
import tyrell.callisto.base.definition.ExperimentalLibraryApi
import tyrell.callisto.base.definition.LibraryApi
import tyrell.callisto.base.kotlin.dsl.uncheckedCast
import java.io.Serializable
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * [Delegates] is the class that provide general purpose utility methods for defining delegated properties.
 *
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public object Delegates {

    /**
     * Creates property delegate that defines nullable property that will have `null` value by default,
     * but only not-null values can be provided via setter of given property.
     */
    @DelicateLibraryApi
    public inline fun <T : Any> nullableWithNotNullSetter(): ReadWriteProperty<Any?, T?> =
        NullableVarWithNotNullSetter()

    /**
     * Creates serializable property delegate that stores state transitively.
     *
     * Lazy value will be initialized using [initializer] when:
     * 1. it is accessed the first time;
     * 2. it is accessed the first time after object deserialization.
     *
     * @since 0.1.0
     */
    @ExperimentalLibraryApi
    public inline fun <T : Any> transientLazy(noinline initializer: () -> T): Lazy<T> =
        TransientLazy(initializer)
}

@PublishedApi
internal class NullableVarWithNotNullSetter<T : Any> : ReadWriteProperty<Any?, T?> {

    @Volatile
    private var value: T? = null

    override fun getValue(thisRef: Any?, property: KProperty<*>): T? = value

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        this.value = requireNotNull(value) {
            "Property ${property.name} was tried to be assigned with 'null' value."
        }
    }
}

@PublishedApi
internal class TransientLazy<out T : Any>(
    private val initializer: () -> T,
) : Lazy<T>, Serializable {

    @Volatile @Transient
    private var _value: Any? = null

    override val value: T
        get() {
            val value1 = _value
            if (value1 != null) {
                return uncheckedCast(value1)
            }
            return synchronized(this) {
                val value2 = _value
                if (value2 != null) {
                    uncheckedCast(value2)
                } else {
                    val typedValue = initializer()
                    _value = typedValue
                    typedValue
                }
            }
        }

    override fun isInitialized(): Boolean = (_value != null)

    override fun toString(): String = if (isInitialized()) {
        value.toString()
    } else {
        "Lazy value not initialized yet."
    }
}
