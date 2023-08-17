package tyrell.callisto.data.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import mu.KLogger
import org.junit.jupiter.api.Nested
import tyrell.callisto.base.kotlin.properties.LoggingDelegates
import tyrell.callisto.data.typing.BaseParameterDefinition
import tyrell.callisto.data.typing.type.BasicDataType
import tyrell.callisto.data.typing.type.CollectionDataType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.expect

class ThreadSafeObjectsTests {

    private val logger: KLogger by LoggingDelegates.instanceLogger()

    @Nested
    inner class `BaseParameterDefinition Tests` {

        @Test
        fun `JSON serialization successful`() {
            val value = BaseParameterDefinition.build {
                code = "simpleParameter"
                dataType = CollectionDataType.List(BasicDataType.Long)
                mandatory = true
                nullable = false
                defaultValue = listOf(1L, 2L, 3L)
            }
            val stringValue = ThreadSafeObjects.JsonMappers.plainMapper.writeValueAsString(value)
            val expectedStringValue = buildString {
                append('{')
                    .append("\"code\":\"simpleParameter\",")
                    .append("\"dataType\":\"List<Long>\",")
                    .append("\"mandatory\":true,")
                    .append("\"nullable\":false,")
                    .append("\"defaultValue\":[1,2,3]")
                append('}')
            }

            assertEquals(
                expectedStringValue, stringValue,
                "Failed to serialize instance of [BaseParameterDefinition] into JSON",
            )
        }

        @Test
        fun `XML serialization successful`() {
            val value = BaseParameterDefinition.build {
                code = "simpleParameter"
                dataType = CollectionDataType.List(BasicDataType.Long)
                mandatory = true
                nullable = false
                defaultValue = listOf(1L, 2L, 3L)
            }
            val stringValue = ThreadSafeObjects.XmlMappers.plainMapper.writeValueAsString(value)
            val expectedStringValue = """
                <BaseParameterDefinition>
                  <code>simpleParameter</code>
                  <dataType>List[Long]</dataType>
                  <mandatory>true</mandatory>
                  <nullable>false</nullable>
                  <defaultValue>1</defaultValue>
                  <defaultValue>2</defaultValue>
                  <defaultValue>3</defaultValue>
                </BaseParameterDefinition>

            """.trimIndent()

            assertEquals(
                expectedStringValue, stringValue,
                "Failed to serialize instance of [BaseParameterDefinition] into XML",
            )
        }

        @Test
        fun `serialization-deserialization complies`() {
            val value: BaseParameterDefinition = BaseParameterDefinition.build {
                code = "simpleParameter"
                dataType = BasicDataType.String
                mandatory = true
            }

            onAllMappers { testSerializeDeserializeEquality(value) }
        }

        @Test
        fun `serialization-deserialization with CollectionDataType complies`() {
            val value: BaseParameterDefinition = BaseParameterDefinition.build {
                code = "collectionParameter"
                dataType = CollectionDataType.List(BasicDataType.String)
                mandatory = true
            }

            onAllMappers { testSerializeDeserializeEquality(value) }
        }
    }

    private fun <T> onAllMappers(closure: ObjectMapper.() -> T): List<T> = sequence {
        yieldAll(onJsonMappers(closure))
        yieldAll(onXmlMappers(closure))
    }.toList()

    private fun <T> onXmlMappers(closure: ObjectMapper.() -> T): List<T> = XML_MAPPERS.map(closure)

    private fun <T> onJsonMappers(closure: ObjectMapper.() -> T): List<T> = JSON_MAPPERS.map(closure)

    private fun ObjectMapper.testSerializeDeserializeEquality(inputValue: BaseParameterDefinition?) {
        val mapper: ObjectMapper = this

        expect(inputValue, failureMessage("$inputValue  ($mapper)")) {
            logger.trace { inputValue }

            val outputString: String = mapper.writeValueAsString(inputValue)
            logger.trace { outputString }

            mapper.readValue(outputString)
        }
    }

    private fun failureMessage(description: String): String =
        "Serialization-deserialization test equality failure: $description"

    private companion object {

        val JSON_MAPPERS: List<ObjectMapper> = listOf(
            ThreadSafeObjects.JsonMappers.plainMapper,
            ThreadSafeObjects.JsonMappers.hibernateMapper,
        )

        val XML_MAPPERS: List<ObjectMapper> = listOf(
            ThreadSafeObjects.XmlMappers.plainMapper,
            ThreadSafeObjects.XmlMappers.hibernateMapper,
        )
    }
}
