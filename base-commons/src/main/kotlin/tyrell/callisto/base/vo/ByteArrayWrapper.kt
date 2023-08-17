package tyrell.callisto.base.vo

import tyrell.callisto.base.definition.DelicateLibraryApi
import tyrell.callisto.base.definition.ExperimentalLibraryApi
import tyrell.callisto.base.util.compareValues
import java.nio.ByteBuffer

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@ExperimentalLibraryApi @DelicateLibraryApi
public class ByteArrayWrapper private constructor(

    private val bytes: ByteArray,
) : Iterable<Byte>, Comparable<ByteArrayWrapper> {

    private val buffer: ByteBuffer by lazy { ByteBuffer.wrap(bytes) }

    public val size: Long get() = this.bytes.size.toLong()

    public fun isEmpty(): Boolean = this.bytes.isEmpty()

    public fun unwrap(): ByteArray = this.bytes

    public fun buffer(): ByteBuffer = this.buffer

    override fun iterator(): Iterator<Byte> = bytes.iterator()

    override fun compareTo(other: ByteArrayWrapper): Int = compareValues(bytes, other.bytes)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ByteArrayWrapper) return false

        if (size != other.size) return false
        if (!bytes.contentEquals(other.bytes)) return false

        return true
    }

    override fun hashCode(): Int = bytes.contentHashCode()

    override fun toString(): String = "ByteArrayWrapper(size=${bytes.size})"

    public companion object {

        @JvmStatic
        public fun ByteArray.wrap(): ByteArrayWrapper = ByteArrayWrapper(this)

        @JvmStatic
        public fun ByteBuffer.asWrapper(): ByteArrayWrapper = ByteArrayWrapper(this.array())
    }
}
