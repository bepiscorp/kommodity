package tyrell.callisto.signal.unix.model

import tyrell.callisto.base.definition.InternalLibraryApi
import tyrell.callisto.signal.model.DomainSignal

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@InternalLibraryApi
public data class UnixDomainSignal(val value: JvmSignal) : DomainSignal
