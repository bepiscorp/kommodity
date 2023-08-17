package tyrell.callisto.base.kotlin.extension

import tyrell.callisto.base.definition.LibraryApi

/**
 * Returns value itself
 * @author Mikhail Gostev
 * @since 0.1.0
 */
@LibraryApi public fun <T : Any> T.identity(): T = this
