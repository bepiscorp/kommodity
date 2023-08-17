package tyrell.callisto.base.util

import tyrell.callisto.base.definition.ExperimentalLibraryApi

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@ExperimentalLibraryApi
public fun compareValues(a: ByteArray?, b: ByteArray?): Int {
    if (a === b) return 0
    if (a == null) return 1
    if (b == null) return -1

    if (a.size != b.size) {
        return compareValues(a.size, b.size)
    }

    for ((aByte, bByte) in a.asSequence().zip(b.asSequence())) {
        val compareResult: Int = compareValues(aByte, bByte)
        if (compareResult != 0) {
            return compareResult
        }
    }

    return 0
}
