package tyrell.callisto.hibernate.enums

import mu.KLogger
import tyrell.callisto.base.definition.LibraryApi
import tyrell.callisto.base.kotlin.dsl.uncheckedCast
import tyrell.callisto.base.kotlin.properties.LoggingDelegates
import java.lang.reflect.ParameterizedType
import javax.persistence.AttributeConverter

/**
 * JPA converter for particular [JpaEnum] class implements conversion of that enum to its [String] db representation and back.
 *
 * @author Mikhail Gostev
 *
 * @author Mikhail Gostev
 * @since 0.0.1
 * @see AttributeConverter
 */
@LibraryApi
@Suppress("ConverterNotAnnotatedInspection")
public open class JpaEnumConverter<T> protected constructor() : AttributeConverter<T, String>
    where T : JpaEnum, T : Enum<T> {

    private val logger: KLogger by LoggingDelegates.instanceLogger()

    private val enumClass: Class<T>

    init {
        val genericSuperclass = this.javaClass.genericSuperclass as ParameterizedType
        this.enumClass = uncheckedCast(genericSuperclass.actualTypeArguments[0])
    }

    override fun convertToDatabaseColumn(attribute: T?): String? = attribute?.dbKey

    override fun convertToEntityAttribute(dbData: String?): T? {
        if (dbData == null) {
            return null
        }

        val enumValue: T? = enumClass.enumConstants.find { it.dbKey == dbData }
        if (enumValue != null) {
            return enumValue
        }

        throw IllegalArgumentException(
            "Invalid enum value [$dbData] of enum class [${enumClass.simpleName}]",
        ).let(logger::throwing)
    }
}
