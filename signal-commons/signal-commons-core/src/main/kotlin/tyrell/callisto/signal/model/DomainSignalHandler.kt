package tyrell.callisto.signal.model

import tyrell.callisto.base.definition.InternalLibraryApi

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@InternalLibraryApi
public typealias DomainSignalHandler = (DomainSignal) -> Unit
