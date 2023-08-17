package tyrell.callisto.signal.unix.model

import tyrell.callisto.base.definition.LibraryApi

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public enum class UnixSignalAction {

    CONTINUE,

    CORE,

    IGNORE,

    STOP,

    TERMINATE
}
