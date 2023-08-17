package tyrell.callisto.hibernate.converter

import tyrell.callisto.base.definition.LibraryApi
import tyrell.callisto.base.messaging.enums.ResponseTypeCategory
import javax.persistence.AttributeConverter
import javax.persistence.Converter

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
@Converter
public class ResponseCategoryConverter : AttributeConverter<ResponseTypeCategory, String> {

    override fun convertToDatabaseColumn(attribute: ResponseTypeCategory?): String? = attribute?.code

    override fun convertToEntityAttribute(dbData: String?): ResponseTypeCategory? =
        dbData?.let(ResponseTypeCategory::fromCode)
}
