package tyrell.callisto.data.typing.serialization

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import tyrell.callisto.base.definition.LibraryApi
import tyrell.callisto.data.typing.type.BasicDataType
import tyrell.callisto.data.typing.type.CollectionDataType
import tyrell.callisto.data.typing.type.DataType

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public class DataTypeSerializer : JsonSerializer<DataType<*>>() {

    override fun serialize(value: DataType<*>?, gen: JsonGenerator, serializers: SerializerProvider) {
        when {
            (value == null) -> gen.writeNull()
            gen.isXmlUsed() -> serializeForXml(value, gen)
            else -> serializeForJson(value, gen)
        }
    }

    private fun serializeForXml(value: DataType<*>, gen: JsonGenerator) = when (value) {
        is CollectionDataType -> buildCollectionTypeString(value, diamondOpen = "[", diamondClose = "]")
        is BasicDataType -> buildBasicTypeString(value)
        else -> error("Unsupported kind of data type for value [$value]")
    }.let(gen::writeString)

    private fun serializeForJson(value: DataType<*>, gen: JsonGenerator) = when (value) {
        is CollectionDataType -> buildCollectionTypeString(value, diamondOpen = "<", diamondClose = ">")
        is BasicDataType -> buildBasicTypeString(value)
        else -> error("Unsupported kind of data type for value [$value]")
    }.let(gen::writeString)

    private fun JsonGenerator.isXmlUsed(): Boolean = (codec is XmlMapper)

    private fun buildBasicTypeString(type: BasicDataType<*>) = type::class.simpleName

    private fun buildCollectionTypeString(
        type: CollectionDataType<*>,
        diamondOpen: String,
        diamondClose: String,
    ) = buildString {
        append(type::class.simpleName)
        append(diamondOpen)
        append(type.diamondType.name)
        append(diamondClose)
    }
}
