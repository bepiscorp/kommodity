package tyrell.callisto.hibernate.converter

import tyrell.callisto.base.definition.LibraryApi
import javax.persistence.AttributeConverter
import javax.persistence.Converter

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
@Converter(autoApply = true)
public class BooleanConverter : AttributeConverter<Boolean?, String?> {

    override fun convertToDatabaseColumn(value: Boolean?): String? = when (value) {
        null -> null
        true -> "T"
        false -> "F"
    }

    override fun convertToEntityAttribute(dbData: String?): Boolean? = if (dbData == null) null else (dbData == "T")
}
