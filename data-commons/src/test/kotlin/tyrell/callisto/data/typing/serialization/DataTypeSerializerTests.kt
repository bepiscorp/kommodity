@file:OptIn(ExperimentalLibraryApi::class)

package tyrell.callisto.data.typing.serialization

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.spyk
import io.mockk.verify
import tyrell.callisto.base.definition.ExperimentalLibraryApi
import tyrell.callisto.data.typing.type.BasicDataType
import tyrell.callisto.data.typing.type.CollectionDataType
import tyrell.callisto.data.typing.type.DataType

class DataTypeSerializerTests : FunSpec({
    context("Basic data types tests") {
        withData(
            nameFn = SerializerTestCase::toString,
            SerializerTestCase(BasicDataType.Boolean, "Boolean"),
            SerializerTestCase(BasicDataType.Double, "Double"),
            SerializerTestCase(BasicDataType.Integer, "Integer"),
            SerializerTestCase(BasicDataType.Long, "Long"),
            SerializerTestCase(BasicDataType.String, "String"),
            SerializerTestCase(BasicDataType.Date, "Date"),
            SerializerTestCase(BasicDataType.Time, "Time"),
            SerializerTestCase(BasicDataType.DateTime, "DateTime"),
            SerializerTestCase(BasicDataType.Timestamp, "Timestamp"),
        ) { (type, expectedResult) ->
            testDataTypeSerialization(type, expectedResult)
            testDataTypeSerialization(
                type, expectedResult,
                generatorFn = { every { codec } returns mockk<XmlMapper>() },
            )
        }
    }

    context("Collection data types tests") {
        withData(
            nameFn = SerializerTestCase::toString,
            SerializerTestCase(CollectionDataType.Set(BasicDataType.Boolean), "Set<Boolean>"),
            SerializerTestCase(CollectionDataType.Set(BasicDataType.DateTime), "Set<DateTime>"),
            SerializerTestCase(CollectionDataType.List(BasicDataType.Time), "List<Time>"),
            SerializerTestCase(CollectionDataType.List(BasicDataType.Timestamp), "List<Timestamp>"),
        ) { (type, expectedResult) -> testDataTypeSerialization(type, expectedResult) }
    }

    context("Collection data types XML tests") {
        withData(
            nameFn = SerializerTestCase::toString,
            SerializerTestCase(CollectionDataType.Set(BasicDataType.Boolean), "Set[Boolean]"),
            SerializerTestCase(CollectionDataType.Set(BasicDataType.DateTime), "Set[DateTime]"),
            SerializerTestCase(CollectionDataType.List(BasicDataType.Time), "List[Time]"),
            SerializerTestCase(CollectionDataType.List(BasicDataType.Timestamp), "List[Timestamp]"),
        ) { (type, expectedResult) ->
            testDataTypeSerialization(
                type, expectedResult,
                generatorFn = { every { codec } returns mockk<XmlMapper>() },
            )
        }
    }
},)

private fun testDataTypeSerialization(
    type: DataType<*>,
    expectedResult: String?,
    generatorFn: JsonGenerator.() -> Unit = {},
) {
    val generator = spyk<JsonGenerator>() { generatorFn() }
    val provider = spyk<SerializerProvider>()
    DataTypeSerializer().serialize(type, generator, provider)

    val actualResultSlot = slot<String>()
    verify { generator.writeString(capture(actualResultSlot)) }
    actualResultSlot.captured shouldBe expectedResult

    confirmVerified(provider)
}

private data class SerializerTestCase(
    val dataType: DataType<*>,
    val expectedResult: String?,
)
