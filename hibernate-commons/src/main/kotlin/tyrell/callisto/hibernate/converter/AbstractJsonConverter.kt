package tyrell.callisto.hibernate.converter

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import mu.KLogger
import tyrell.callisto.base.definition.LibraryApi
import tyrell.callisto.base.kotlin.dsl.uncheckedCast
import tyrell.callisto.base.kotlin.properties.LoggingDelegates
import tyrell.callisto.data.util.ThreadSafeObjects
import java.io.IOException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import javax.persistence.AttributeConverter

/**
 * Class [AbstractJsonConverter] provides implementation
 *
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
@Suppress("ConverterNotAnnotatedInspection")
public abstract class AbstractJsonConverter<T> : AttributeConverter<T, String?> {

    private val logger: KLogger by LoggingDelegates.instanceLogger()

    protected val pojoClass: Class<T>

    protected open val mapper: ObjectMapper = ThreadSafeObjects.JsonMappers.hibernateMapper

    init {
        val genericSuperclass: ParameterizedType = (javaClass.genericSuperclass as ParameterizedType)
        this.pojoClass = when (val type: Type = genericSuperclass.actualTypeArguments.first()) {
            is Class<*> -> type
            is ParameterizedType -> type.rawType
            else -> throw IllegalStateException("Unable to get generic parameter type for [${type.typeName}]")
                .let(logger::throwing)
        }.let(::uncheckedCast)
    }

    override fun convertToDatabaseColumn(attribute: T?): String? {
        if (attribute == null) {
            return null // Nothing to do
        }

        val dbData: String? = try {
            mapper.writeValueAsString(attribute)
        } catch (ex: JsonProcessingException) {
            logger.warn(ex) { "Failed to serialize object into JSON: ${ex.message}" }
            null
        }

        return dbData
    }

    override fun convertToEntityAttribute(dbData: String?): T? {
        if (dbData == null) {
            return null // Nothing to do
        }

        val attribute: T? = try {
            mapper.readValue(dbData, pojoClass)
        } catch (ex: IOException) {
            logger.warn(ex) { "Failed to deserialize object from JSON: ${ex.message}" }
            null
        }

        return attribute
    }
}
