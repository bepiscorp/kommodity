package tyrell.callisto.data.typing.type

import tyrell.callisto.base.definition.ExperimentalLibraryApi
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.KTypeProjection
import kotlin.reflect.full.createType

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@ExperimentalLibraryApi
public sealed class CollectionDataType<out C : Collection<*>> : AbstractDataType<C> {

    final override val name: String

    public val diamondType: DataType<*>

    private constructor(
        collectionClass: KClass<C>,
        diamondType: DataType<*>,
    ) : super(buildCollectionType(collectionClass, diamondType)) {
        this.name = buildCollectionDataTypeName(this, diamondType)
        this.diamondType = diamondType
    }

    public class List<T : Any>(diamondType: DataType<T>) : CollectionDataType<KList<*>>(KList::class, diamondType)

    public class Set<T : Any>(diamondType: DataType<T>) : CollectionDataType<KSet<*>>(KSet::class, diamondType)
}

private fun buildCollectionDataTypeName(
    collectionDataType: CollectionDataType<*>,
    genericDataType: DataType<*>,
): String = buildString {
    append(collectionDataType::class.simpleName)
    append('<')
    append(genericDataType.name)
    append('>')
}

private fun buildCollectionType(
    collectionClass: KClass<*>,
    diamondType: DataType<*>,
): KType = collectionClass::class.createType(
    listOf(
        KTypeProjection.invariant(diamondType.valueType),
    ),
)

private typealias KList<T> = List<T>
private typealias KSet<T> = Set<T>
