package tyrell.callisto.data.typing.converter

import tyrell.callisto.base.definition.InternalLibraryApi
import tyrell.callisto.data.typing.type.DataType

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@InternalLibraryApi
public abstract class AbstractDataTypeConverter<F : Any, FT : DataType<F>, T : Any, TT : DataType<T>> constructor(

    override val fromDataType: FT,

    override val toDataType: TT,

) : DataTypeConverter<F, FT, T, TT> {

    override fun toString(): String = this::class.simpleName + "(fromDataType=$fromDataType, toDataType=$toDataType)"
}
