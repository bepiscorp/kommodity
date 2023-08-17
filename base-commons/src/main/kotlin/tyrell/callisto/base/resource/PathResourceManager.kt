package tyrell.callisto.base.resource

import tyrell.callisto.base.definition.LibraryApi
import tyrell.callisto.base.resource.exception.ResourceProcessingException
import tyrell.callisto.base.resource.vo.LoadedResource
import java.nio.file.Path

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public interface PathResourceManager<out V : Any> : ResourceManager<V> {

    /**
     * Checks whether a resource with the given path exists.
     *
     * @param resourcePath resource path
     *
     * @return `true` if the resource exists, `false` otherwise
     *
     * @throws ResourceProcessingException when the check can not be performed
     */
    public suspend fun exists(resourcePath: Path): Boolean

    /**
     * Loads resource [V] by resource path.
     *
     * @param resourcePath path to resource
     *
     * @return loaded resource contents with [V] value or `null` if specified identifier is not regular file.
     *
     * @throws ResourceLoadingException If an error occurred while reading file.
     */
    @Throws(ResourceProcessingException::class)
    public suspend fun load(resourcePath: Path): LoadedResource<V>?

    /**
     * Loads resource by its path.
     *
     * This method is thread-safe.
     *
     * @param resourcePath path to file
     * @param isMandatory if resource is mandatory
     *
     * @return file contents as [ByteArray] or `null` if specified identifier is not regular file.
     *
     * @throws ResourceLoadingException if an error occurred while reading file or if mandatory resource was not found
     */
    @Throws(ResourceProcessingException::class)
    public suspend fun load(resourcePath: Path, isMandatory: Boolean): LoadedResource<V>?
}
