package tyrell.callisto.data.typing.type

import com.fasterxml.jackson.annotation.JsonIgnore
import tyrell.callisto.base.definition.ExperimentalLibraryApi
import kotlin.reflect.KType

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@ExperimentalLibraryApi
public interface DataType<out V : Any> {

    public val name: String

    @get:JsonIgnore
    @get:JvmSynthetic
    public val valueType: KType
}
