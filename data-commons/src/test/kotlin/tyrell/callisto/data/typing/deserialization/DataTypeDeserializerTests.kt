@file:OptIn(ExperimentalLibraryApi::class)

package tyrell.callisto.data.typing.deserialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import tyrell.callisto.base.definition.ExperimentalLibraryApi
import tyrell.callisto.data.typing.type.BasicDataType
import tyrell.callisto.data.typing.type.CollectionDataType
import tyrell.callisto.data.typing.type.DataType

class DataTypeDeserializerTests : FunSpec({
    context("Basic data types tests") {
        withData(
            nameFn = DeserializerTestCase::toString,
            DeserializerTestCase("Boolean", BasicDataType.Boolean),
            DeserializerTestCase("Double", BasicDataType.Double),
            DeserializerTestCase("Integer", BasicDataType.Integer),
            DeserializerTestCase("Long", BasicDataType.Long),
            DeserializerTestCase("String", BasicDataType.String),
            DeserializerTestCase("Date", BasicDataType.Date),
            DeserializerTestCase("Time", BasicDataType.Time),
            DeserializerTestCase("DateTime", BasicDataType.DateTime),
            DeserializerTestCase("Timestamp", BasicDataType.Timestamp),
        ) { (type, expectedResult) ->
            testDataTypeDeserialization(type, expectedResult)
            testDataTypeDeserialization(
                type, expectedResult,
                parserFn = { every { codec } returns mockk<XmlMapper>() },
            )
        }
    }

    context("Collection data types tests") {
        withData(
            nameFn = DeserializerTestCase::toString,
            DeserializerTestCase("Set<Boolean>", CollectionDataType.Set(BasicDataType.Boolean)),
            DeserializerTestCase("Set<DateTime>", CollectionDataType.Set(BasicDataType.DateTime)),
            DeserializerTestCase("List<Time>", CollectionDataType.List(BasicDataType.Time)),
            DeserializerTestCase("List<Timestamp>", CollectionDataType.List(BasicDataType.Timestamp)),
        ) { (type, expectedResult) -> testDataTypeDeserialization(type, expectedResult) }
    }

    context("Collection data types XML tests") {
        withData(
            nameFn = DeserializerTestCase::toString,
            DeserializerTestCase("Set[Boolean]", CollectionDataType.Set(BasicDataType.Boolean)),
            DeserializerTestCase("Set[DateTime]", CollectionDataType.Set(BasicDataType.DateTime)),
            DeserializerTestCase("List[Time]", CollectionDataType.List(BasicDataType.Time)),
            DeserializerTestCase("List[Timestamp]", CollectionDataType.List(BasicDataType.Timestamp)),
        ) { (type, expectedResult) ->
            testDataTypeDeserialization(
                type, expectedResult,
                parserFn = { every { codec } returns mockk<XmlMapper>() },
            )
        }
    }
},)

private fun testDataTypeDeserialization(
    serializedValue: String?,
    expectedResult: DataType<*>?,
    parserFn: JsonParser.() -> Unit = {},
) {
    val parser = mockk<JsonParser>() {
        parserFn()
        every { valueAsString } returns serializedValue
    }
    val context = spyk(DefaultDeserializationContext.Impl(mockk()))

    val actualResult = DataTypeDeserializer().deserialize(parser, context)
    actualResult shouldBe expectedResult

    confirmVerified(context)
}

private data class DeserializerTestCase(
    val serializedValue: String?,
    val expectedResult: DataType<*>?,
)
