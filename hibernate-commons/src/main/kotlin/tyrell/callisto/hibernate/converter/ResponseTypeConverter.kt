package tyrell.callisto.hibernate.converter

import tyrell.callisto.base.definition.LibraryApi
import tyrell.callisto.base.messaging.enums.ResponseType
import javax.persistence.AttributeConverter
import javax.persistence.Converter

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
@Converter
public class ResponseTypeConverter : AttributeConverter<ResponseType, String> {

    override fun convertToDatabaseColumn(attribute: ResponseType?): String? = attribute?.dbKey

    override fun convertToEntityAttribute(dbData: String?): ResponseType? = dbData?.let { ResponseType.fromDbKey(it) }
}
