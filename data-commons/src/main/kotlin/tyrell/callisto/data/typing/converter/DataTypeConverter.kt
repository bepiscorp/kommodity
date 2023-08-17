package tyrell.callisto.data.typing.converter

import tyrell.callisto.base.definition.InternalLibraryApi
import tyrell.callisto.data.typing.type.DataType

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@InternalLibraryApi
public interface DataTypeConverter<
    F : Any, FDT : DataType<F>,
    T : Any, TDT : DataType<T>,
    > {

    public val fromDataType: FDT

    public val toDataType: TDT

    public fun convert(value: F?): T?
}
