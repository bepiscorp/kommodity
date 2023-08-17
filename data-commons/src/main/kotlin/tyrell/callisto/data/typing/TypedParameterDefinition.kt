package tyrell.callisto.data.typing

import tyrell.callisto.base.definition.ExperimentalLibraryApi
import tyrell.callisto.base.type.ParameterDefinition
import tyrell.callisto.data.typing.type.DataType

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@ExperimentalLibraryApi
public interface TypedParameterDefinition<out V : Any> : ParameterDefinition {

    public val dataType: DataType<V>
}
