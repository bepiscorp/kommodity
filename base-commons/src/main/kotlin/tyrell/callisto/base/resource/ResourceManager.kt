package tyrell.callisto.base.resource

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import tyrell.callisto.base.definition.LibraryApi
import tyrell.callisto.base.resource.exception.ResourceProcessingException
import tyrell.callisto.base.resource.vo.LoadedResource

/**
 * This is base interface that must be implemented by all resource loaders.
 *
 *
 * Implementations of this interface must be thread-safe.
 *
 * @param V Resource class
 *
 * @author Mikhail Gostev
 * @since 0.0.1
 **/
@LibraryApi
public interface ResourceManager<out V : Any> {

    /**
     * Checks whether a resource with the given identifier exists.
     *
     * @param resourceIdentifier resource identifier
     *
     * @return `true` if the resource exists, `false` otherwise
     *
     * @throws ResourceProcessingException when the check can not be performed
     */
    @Throws(ResourceProcessingException::class, UnsupportedOperationException::class)
    public suspend fun exists(resourceIdentifier: String): Boolean {
        throw UnsupportedOperationException()
    }

    /**
     * Loads resource by its identifier.
     *
     * @param resourceIdentifier resource identifier
     *
     * @return loaded resource or `null` if requested resource was not found
     *
     * @throws ResourceProcessingException if an error occurred while loading resource.
     */
    @Throws(ResourceProcessingException::class)
    public suspend fun load(resourceIdentifier: String): LoadedResource<V>?

    /**
     * Loads resource by its identifier.
     *
     * @param resourceIdentifier resource identifier
     * @param isMandatory        if resource is mandatory
     *
     * @return loaded resource
     *
     * @throws ResourceProcessingException if an error occurred while loading resource
     * or if mandatory resource was not found
     */
    @Throws(ResourceProcessingException::class)
    public suspend fun load(resourceIdentifier: String, isMandatory: Boolean): LoadedResource<V>? {
        val resource: LoadedResource<V>? = load(resourceIdentifier)
        if (resource == null && isMandatory) {
            throw ResourceProcessingException("Mandatory resource [$resourceIdentifier] not found")
        }
        return resource
    }

    /**
     * Loads all resources by identifier pattern as list.
     *
     * @param resourceIdentifierPattern identifier pattern
     *
     * @return [List] of loaded resources
     *
     * @throws ResourceProcessingException if an error occurred while loading resources
     * @throws UnsupportedOperationException if ResourceLoader doesn't support pattern based search
     */
    @Throws(ResourceProcessingException::class, UnsupportedOperationException::class)
    public suspend fun loadAllAsList(resourceIdentifierPattern: String): List<LoadedResource<V>> =
        loadAll(resourceIdentifierPattern).toList(ArrayList(32))

    /**
     * Loads all resources by identifier pattern as map, with resource identifier as key.
     *
     * @param resourceIdentifierPattern identifier pattern
     *
     * @return [Map] of loaded resources
     *
     * @throws ResourceProcessingException if an error occurred while loading resources
     * @throws UnsupportedOperationException if ResourceLoader doesn't support pattern based search
     */
    @Throws(ResourceProcessingException::class, UnsupportedOperationException::class)
    public fun loadAll(resourceIdentifierPattern: String): Flow<LoadedResource<V>> {
        throw UnsupportedOperationException()
    }
}
