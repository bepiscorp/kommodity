package tyrell.callisto.data.typing

import tyrell.callisto.base.definition.ExperimentalLibraryApi
import tyrell.callisto.base.type.Parameter

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@ExperimentalLibraryApi
public interface TypedParameter<V : Any> : Parameter<V>, TypedParameterDefinition<V>
