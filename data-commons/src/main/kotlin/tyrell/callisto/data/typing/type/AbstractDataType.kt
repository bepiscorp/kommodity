package tyrell.callisto.data.typing.type

import tyrell.callisto.base.definition.InternalLibraryApi
import kotlin.reflect.KType

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@InternalLibraryApi
public abstract class AbstractDataType<out V : Any>(override val valueType: KType) : DataType<V> {

    private val cachedHashCode: Int by lazy { computeHashCode() }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AbstractDataType<*>) return false

        if (valueType != other.valueType) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int = cachedHashCode

    override fun toString(): String = "Type(name='$name', valueType=$valueType)"

    protected open fun computeHashCode(): Int {
        var result = valueType.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }
}
