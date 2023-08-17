package tyrell.callisto.base.resource.impl

import kotlinx.coroutines.flow.Flow
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component
import tyrell.callisto.base.definition.LibraryApi
import tyrell.callisto.base.resource.PathResourceManager
import tyrell.callisto.base.resource.exception.ResourceLoadingException
import tyrell.callisto.base.resource.vo.LoadedResource
import java.io.IOException
import java.nio.file.Path
import kotlin.io.path.absolute
import kotlin.io.path.absolutePathString
import kotlin.io.path.exists

/**
 * The [PathResourceManager] loads resource as [ByteArray] from file system.
 *
 * File name is resource identifier. If file name is not absolute then it is resolved from current folder,
 * it is not guaranteed which folder is current.
 *
 * Note: This class is thread-safe.
 *
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi @Component
public class BytePathResourceManager : AbstractByteResourceManager(), PathResourceManager<ByteArray> {

    override suspend fun exists(resourceIdentifier: String): Boolean = try {
        val resource: Resource? = getResource(PREFIX, resourceIdentifier)
        (resource != null) && resource.exists()
    } catch (ex: IOException) {
        throw ResourceLoadingException(ex)
    }

    override suspend fun load(resourceIdentifier: String): LoadedResource<ByteArray>? =
        load(PREFIX, resourceIdentifier)

    override fun loadAll(resourceIdentifierPattern: String): Flow<LoadedResource<ByteArray>> =
        loadAll(PREFIX, resourceIdentifierPattern)

    override suspend fun exists(resourcePath: Path): Boolean =
        getFullResourcePath(resourcePath).exists()

    override suspend fun load(resourcePath: Path): LoadedResource<ByteArray>? =
        if (resourcePath.isAbsolute) {
            load(resourcePath.absolutePathString())
        } else {
            load(getFullResourcePath(resourcePath).absolutePathString())
        }

    override suspend fun load(resourcePath: Path, isMandatory: Boolean): LoadedResource<ByteArray>? {
        val loadedResource: LoadedResource<ByteArray>? = load(resourcePath)
        if (isMandatory && (loadedResource == null)) {
            throw ResourceLoadingException("Mandatory resource [$resourcePath] not found").let(logger::throwing)
        }
        return loadedResource
    }

    private fun getFullResourcePath(resourceIdentifier: Path): Path =
        if (resourceIdentifier.isAbsolute) {
            resourceIdentifier.normalize()
        } else {
            resourceIdentifier.absolute().normalize()
        }

    private companion object {

        private const val PREFIX = "file:"
    }
}
