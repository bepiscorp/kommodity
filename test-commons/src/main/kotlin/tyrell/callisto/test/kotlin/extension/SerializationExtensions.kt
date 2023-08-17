package tyrell.callisto.test.kotlin.extension

import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import mu.KLogger
import mu.NamedKLogging
import tyrell.callisto.base.definition.ExperimentalLibraryApi
import tyrell.callisto.data.serialization.ImmutableObjectMapper
import tyrell.callisto.data.util.ThreadSafeObjects
import tyrell.callisto.test.TestException
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.test.assertEquals
import kotlin.test.fail

/**
 * Checks that given [value]:
 * can be serialized using captured [ObjectMapper]
 * can be deserialized from serialized value using captured [ObjectMapper]
 * deserialized value equals [value]
 *
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@ExperimentalLibraryApi
public inline fun <reified T> ObjectMapper.assertCompliesMapping(value: T): T {
    val mapperName: String = (this as? ImmutableObjectMapper)?.name ?: (this::class.java.simpleName)
    val logger: KLogger = NamedKLogging("MappingCompliance[$mapperName]").logger
    val mapper: ObjectMapper = this

    logger.debug { "Before serialization:\n$value" }
    val outputString: String = assertCompliesSerialization(value)
    logger.trace { "After serialization:\n$outputString" }

    return withMapper(mapper) { mapper.readValue<T>(outputString) }
        .also { outputValue ->
            logger.debug { "After deserialization:\n$outputValue" }
            logger.trace { "After deserialization:\n${writeValueAsString(outputValue)}" }

            assertEquals(
                value, outputValue,
                "Serialization-deserialization test equality failure: $value != $outputValue ($this)",
            )
        }
}

/**
 * Checks that given [value] can be serialized into class [T] using captured [ObjectMapper]
 *
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@ExperimentalLibraryApi
public fun <T> ObjectMapper.assertCompliesSerialization(value: T): String {
    val mapperName: String = (this as? ImmutableObjectMapper)?.name ?: (this::class.java.simpleName)
    val logger: KLogger = NamedKLogging("SerializeCompliance[$mapperName]").logger
    val mapper: ObjectMapper = this

    return try {
        withMapper(mapper) { writeValueAsString(value) }
            .also { outputValue -> logger.debug { "After serialization:\n$outputValue" } }
    } catch (ex: Throwable) {
        fail("Serialization compliance failure", ex)
    }
}

/**
 * Checks that given string [value] can be deserialized into class [T] using captured [ObjectMapper]
 *
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@ExperimentalLibraryApi
public inline fun <reified T : Any> ObjectMapper.assertCompliesDeserialization(value: String): T {
    val mapperName: String = (this as? ImmutableObjectMapper)?.name ?: (this::class.java.simpleName)
    val logger: KLogger = NamedKLogging("DeserializeCompliance[$mapperName]").logger
    val mapper: ObjectMapper = this

    return try {
        withMapper(mapper) { readValue<T>(value) }
            .also { outputValue ->
                logger.debug { "After deserialization:\n$outputValue" }
                runCatching { logger.trace { "After deserialization:\n${writeValueAsString(outputValue)}" } }
            }
    } catch (ex: Throwable) {
        fail("Deserialization compliance failure", ex)
    }
}

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@ExperimentalLibraryApi
public fun <T> withXmlMappers(closure: ObjectMapper.() -> T): List<T> = XML_MAPPERS.map { withMapper(it, closure) }

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@ExperimentalLibraryApi
public fun <T> withJsonMappers(closure: ObjectMapper.() -> T): List<T> = JSON_MAPPERS.map { withMapper(it, closure) }

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@ExperimentalLibraryApi
public fun <T> withMapper(mapper: ObjectMapper, closure: (ObjectMapper.() -> T)): T = runCatching {
    with(mapper) { closure() }
}.onFailure { throwable: Throwable ->
    val mapperName: String = (mapper as? ImmutableObjectMapper)?.name ?: (mapper::class.java.simpleName)

    val message: String = "Routine with mapper [$mapperName] has failed with exception: " + when {
        (throwable is JsonMappingException) -> throwable.message
        else -> "Unknown error"
    }

    throw TestException(message, throwable)
}.getOrThrow()

private val JSON_MAPPERS: List<ObjectMapper> = run {
    ThreadSafeObjects.JsonMappers::class.declaredMemberProperties.asSequence()
        .filterIsInstance<KProperty1<ThreadSafeObjects.JsonMappers, ObjectMapper>>()
        .map { it.get(ThreadSafeObjects.JsonMappers) }
        .filterIsInstance<ImmutableObjectMapper>()
        .toList()
}

private val XML_MAPPERS: List<ObjectMapper> = run {
    ThreadSafeObjects.XmlMappers::class.declaredMemberProperties.asSequence()
        .filterIsInstance<KProperty1<ThreadSafeObjects.XmlMappers, ObjectMapper>>()
        .map { it.get(ThreadSafeObjects.XmlMappers) }
        .filterIsInstance<ImmutableObjectMapper>()
        .toList()
}
