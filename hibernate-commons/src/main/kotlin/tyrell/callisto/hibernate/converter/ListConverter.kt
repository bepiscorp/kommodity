package tyrell.callisto.hibernate.converter

import com.fasterxml.jackson.databind.JavaType
import tyrell.callisto.base.definition.DelicateLibraryApi
import tyrell.callisto.base.definition.LibraryApi

/**
 * TODO: Document why this is needed (AbstractJsonConverter<List<T>> bugs deserializing T elements into strings)
 *
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi @DelicateLibraryApi
public open class ListConverter<V> : AbstractJsonConverter<List<V>>() {

    private val type: JavaType = mapper.typeFactory.constructCollectionType(MutableList::class.java, pojoClass)

    override fun convertToDatabaseColumn(attribute: List<V>?): String? =
        if (attribute == null || attribute.isEmpty()) null
        else super.convertToDatabaseColumn(attribute)

    override fun convertToEntityAttribute(dbData: String?): List<V>? =
        if (dbData.isNullOrEmpty()) null
        else mapper.readValue(dbData, type)
}
