package tyrell.callisto.data.typing.converter

import tyrell.callisto.base.definition.InternalLibraryApi
import tyrell.callisto.data.typing.type.DataType

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@InternalLibraryApi
public class PlainDataTypeConverter<F : Any, T : Any> constructor(

    fromType: DataType<F>,

    toType: DataType<T>,

    private val converterClosure: ((F?) -> T?),
) : AbstractDataTypeConverter<F, DataType<F>, T, DataType<T>>(fromType, toType) {

    override fun convert(value: F?): T? = converterClosure.invoke(value)
}
