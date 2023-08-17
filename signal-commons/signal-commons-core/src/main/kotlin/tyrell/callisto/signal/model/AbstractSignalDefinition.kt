package tyrell.callisto.signal.model

import tyrell.callisto.base.definition.LibraryApi

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public abstract class AbstractSignalDefinition(override val type: String) : SignalDefinition
