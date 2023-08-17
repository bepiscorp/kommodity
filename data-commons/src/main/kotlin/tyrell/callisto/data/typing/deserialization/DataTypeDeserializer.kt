package tyrell.callisto.data.typing.deserialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import tyrell.callisto.base.definition.LibraryApi
import tyrell.callisto.base.kotlin.dsl.uncheckedCast
import tyrell.callisto.data.typing.type.BasicDataType
import tyrell.callisto.data.typing.type.CollectionDataType
import tyrell.callisto.data.typing.type.DataType
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.primaryConstructor

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public class DataTypeDeserializer : JsonDeserializer<DataType<*>>() {

    override fun deserialize(p: JsonParser, ctx: DeserializationContext): DataType<*>? {
        val stringValue: String = p.valueAsString ?: return null

        val diamondQualifier: DiamondQualifier? = parseDiamondQualifierOrNull(stringValue)
        if (diamondQualifier != null) {
            return resolveCollectionDataTypeOrNull(diamondQualifier)
                ?: throw ctx.weirdStringException(
                    stringValue, DataType::class.java,
                    "Unknown value $stringValue",
                )
        }

        return resolveSealedObjectOrNull(stringValue)
            ?: throw ctx.weirdStringException(
                stringValue, DataType::class.java,
                "Unknown value $stringValue",
            )
    }

    private fun resolveSealedObjectOrNull(name: String): DataType<*>? {
        return BasicDataType::class
            .sealedSubclasses
            .firstOrNull { it.simpleName == name }
            ?.objectInstance
    }

    private fun <T : Any> KClass<T>.isCollectionDataType(): Boolean {
        /**
         * Each [CollectionDataType] implementation **MUST** have
         * a primary constructor of a single [DataType] argument
         */
        if (primaryConstructor == null) {
            return false
        }

        /**
         * Each [CollectionDataType] implementation **MUST NOT** be an instance
         */
        if (objectInstance != null) {
            return false
        }

        /**
         * Super type of [CollectionDataType] implementation **MUST** be a [CollectionDataType]
         */
        val superClass: KClass<*> = supertypes.asSequence()
            .map(KType::classifier)
            .filterIsInstance<KClass<*>>()
            .first()
        if (superClass != CollectionDataType::class) {
            return false
        }

        return true
    }

    @Suppress("FoldInitializerAndIfToElvis")
    private fun resolveCollectionDataTypeOrNull(qualifier: DiamondQualifier): CollectionDataType<*>? {
        val collectionDataTypeClass: KClass<out CollectionDataType<*>>? = CollectionDataType::class.sealedSubclasses
            .filter { it.isCollectionDataType() }
            .map { uncheckedCast<KClass<CollectionDataType<*>>>(it) }
            .find { it.simpleName == qualifier.collectionDataTypeName }

        if (collectionDataTypeClass == null) {
            return null
        }

        val diamondSealedValue: DataType<*>? = resolveSealedObjectOrNull(qualifier.genericDataTypeName)
        if (diamondSealedValue == null) {
            return null
        }

        return collectionDataTypeClass.primaryConstructor!!.call(diamondSealedValue)
    }

    private fun parseDiamondQualifierOrNull(value: String): DiamondQualifier? {
        val match: MatchResult? = DIAMOND_QUALIFIER_REGEX.find(value)
        if (match == null) {
            return null
        }

        val wrapper: String = match.groups["wrapper"]!!.value
        val diamond: String = match.groups["diamond"]!!.value

        if (DIAMOND_QUALIFIER_REGEX.matches(diamond)) {
            throw IllegalStateException("Nested diamond qualifiers like [List<List<Int>>] are not supported")
        }

        return DiamondQualifier(
            collectionDataTypeName = wrapper,
            genericDataTypeName = diamond,
        )
    }

    public companion object {

        private val DIAMOND_QUALIFIER_REGEX = Regex(
            "^(?<wrapper>[A-Z][A-Za-z]+)[<\\[](?<diamond>[A-Z][A-Za-z]+)[>\\]]$",
        )

        private data class DiamondQualifier(
            val collectionDataTypeName: String,
            val genericDataTypeName: String,
        )
    }
}
