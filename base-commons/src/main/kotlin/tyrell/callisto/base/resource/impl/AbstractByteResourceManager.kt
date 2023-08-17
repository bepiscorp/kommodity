package tyrell.callisto.base.resource.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.runInterruptible
import mu.KLogger
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.core.io.buffer.DefaultDataBufferFactory
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import reactor.core.publisher.Flux
import tyrell.callisto.base.definition.InternalLibraryApi
import tyrell.callisto.base.kotlin.extension.forEachAsync
import tyrell.callisto.base.kotlin.properties.LoggingDelegates
import tyrell.callisto.base.resource.ResourceManager
import tyrell.callisto.base.resource.exception.ResourceProcessingException
import tyrell.callisto.base.resource.vo.LoadedResource
import java.io.IOException
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.system.measureTimeMillis

/**
 * The [AbstractByteResourceManager] class implements loading abstract resource to byte array.
 *
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@InternalLibraryApi
public abstract class AbstractByteResourceManager protected constructor() : ResourceManager<ByteArray> {

    protected val logger: KLogger by LoggingDelegates.instanceLogger()

    private val patternResolver: PathMatchingResourcePatternResolver = PathMatchingResourcePatternResolver()

    /**
     * Loads resource as [ByteArray] from located classpath [Resource]
     *
     * @param resource pre-located resource
     *
     * @return resource contents as [ByteArray]
     *
     * @throws ResourceProcessingException if an error occurred while reading resource.
     */
    protected suspend fun load(resource: Resource?): LoadedResource<ByteArray>? = coroutineScope closure@{
        if (resource == null) {
            return@closure null
        }

        val resourceBytes = resource.getBytes()
        if (resourceBytes == null) {
            val fullResourcePath = resource.getFullResourcePath()
            logger.trace { "Could not resolve bytes for resource [$fullResourcePath]" }
            return@closure null
        }

        val deferredScheme = async { resource.getScheme() }
        val deferredFullResourcePath = async { resource.getFullResourcePath() }
        val deferredLastModified = async { resource.getLastModified() }

        val scheme: String = deferredScheme.await()
        val fullResourcePath: String = deferredFullResourcePath.await()
        val lastModified: LocalDateTime? = deferredLastModified.await()

        val loadedResource: LoadedResource<ByteArray> = LoadedResource.build {
            this.value = resourceBytes
            this.identifier = fullResourcePath
            this.schema = scheme
            this.fullResourcePath = fullResourcePath
            this.lastModified = lastModified
        }

        logger.trace {
            "Loaded [${loadedResource.value.size}] bytes" +
                " for resource with path [${loadedResource.fullResourcePath}]"
        }
        return@closure loadedResource
    }

    /**
     * Loads resource as [ByteArray] from resource (either file, or classpath).
     * Resources matching the identifier are sorted by source (file, jar, war).
     *
     * @param prefix location prefix
     * @param resourceIdentifier file name
     *
     * @return file contents as [ByteArray] or `null` if specified identifier is not regular file.
     *
     * @throws ResourceProcessingException if an error occurred while reading file.
     * @see PathMatchingResourcePatternResolver
     */
    protected suspend fun load(prefix: String, resourceIdentifier: String): LoadedResource<ByteArray>? {
        try {
            val resource: Resource? = getResource(prefix, resourceIdentifier)
            val loadedResource: LoadedResource<ByteArray>? = load(resource)
            return loadedResource
        } catch (exception: Throwable) {
            throw logger.throwing(
                if (exception is ResourceProcessingException) exception
                else ResourceProcessingException(
                    "Failed to load resource [$resourceIdentifier] due to exception: [${exception.message}]",
                    exception,
                ),
            )
        }
    }

    /**
     * Loads all resources into flow of [ByteArray] wrapped with [LoadedResource].
     * Includes either files, or classpath.
     *
     * @param prefix location prefix
     * @param resourceIdentifierPattern resource name
     *
     * @return resource contents as map of [ByteArray]
     *
     * @throws ResourceProcessingException if an error occurred while reading file.
     * @see PathMatchingResourcePatternResolver
     */
    protected fun loadAll(
        prefix: String,
        resourceIdentifierPattern: String,
    ): Flow<LoadedResource<ByteArray>> = channelFlow {
        logger.entry(prefix, resourceIdentifierPattern)

        var resourcesAmount = 0
        val loadingTime: Long = measureTimeMillis {
            val resources: Array<Resource> = runInterruptible(Dispatchers.IO) {
                patternResolver.getResources(prefix + resourceIdentifierPattern)
            }

            resources.asIterable().forEachAsync { resource ->
                val loadedResource: LoadedResource<ByteArray>? = load(resource)
                if (loadedResource != null) {
                    ++resourcesAmount
                    send(loadedResource)
                }
            }
        }

        if (resourcesAmount > 0) {
            logger.debug { "Loaded [$resourcesAmount] resources for [$loadingTime] milliseconds" }
        } else {
            logger.debug { "No resources found by identifier pattern [$resourceIdentifierPattern]" }
        }
    }.catch { ex: Throwable ->
        logger.catching(ex)
        throw ResourceProcessingException(
            message = "Failed to load resource [$resourceIdentifierPattern] due to exception: [${ex.message}]",
            cause = ex,
        ).let(logger::throwing)
    }

    protected suspend fun getResource(prefix: String, resourceIdentifier: String): Resource? {
        val resources: Array<Resource> = runInterruptible(Dispatchers.IO) {
            patternResolver.getResources(prefix + resourceIdentifier)
        }
        if (resources.isEmpty()) {
            return null
        }

        if (resources.size == 1) {
            return resources.first()
        }

        val maxResource = resources.maxWithOrNull(RESOURCE_PRIORITY_COMPARATOR)!!
        val secondMaxResource = resources.asSequence()
            .dropWhile { it === maxResource }
            .maxWithOrNull(RESOURCE_PRIORITY_COMPARATOR)!!

        if (RESOURCE_PRIORITY_COMPARATOR.compare(maxResource, secondMaxResource) == 0) { // Met duplicate resources
            val failedResources: String = resources.map {
                try {
                    "\"${it.uri}\""
                } catch (ignored: IOException) {
                    logger.catching(ignored)
                    "<could not get resource URI>"
                }
            }.joinToString(separator = ",") { it }

            throw ResourceProcessingException(
                "Ambiguous resource [$resourceIdentifier] definition. Resource list: $failedResources",
            ).let(logger::throwing)
        }

        return maxResource
    }

    private suspend fun Resource.getBytes(): ByteArray? {
        try {
            val matchesReadPreconditions: Boolean = checkReadPreconditions()
            if (!matchesReadPreconditions) {
                return null
            }

            // TODO: Consider implementation with direct buffer (preferDirect == true)
            val bufferFactory: DefaultDataBufferFactory = DefaultDataBufferFactory.sharedInstance

            val buffersFlux: Flux<DataBuffer> = DataBufferUtils.read(this, bufferFactory, DEFAULT_BUFFER_SIZE)
            val dataBuffer: DataBuffer = DataBufferUtils.join(buffersFlux).awaitSingle()

            return dataBuffer.asByteBuffer().array()
        } catch (ex: IOException) {
            throw ResourceProcessingException(ex).let(logger::throwing)
        }
    }

    private suspend fun Resource?.checkReadPreconditions(): Boolean = runInterruptible closure@{
        if (this == null) {
            return@closure false
        }

        val exists: Boolean = this.exists()
        if (!exists) {
            return@closure false
        }

        // Note: That is the reason why nothing is returned for empty file
        val contentLength: Long = this.contentLength()
        val resourceIsEmpty: Boolean = (contentLength == 0L)
        if (resourceIsEmpty) {
            return@closure false
        }

        return@closure true
    }

    private suspend fun Resource.getScheme(): String = runInterruptible { this.uri.scheme }

    private suspend fun Resource.getFullResourcePath(): String {
        val scheme: String = this.getScheme()

        return runInterruptible closure@{
            val isFileSystemResource: Boolean = (this is FileSystemResource) || (scheme == "file")

            val absolutePath: String = if (isFileSystemResource) {
                // Note: This branch speeds up resource loading
                // during not production launches (IDE, tests execution during Gradle build)
                this.file.absolutePath
            } else {
                this.url.toString()
            }

            return@closure absolutePath
        }
    }

    private suspend fun Resource.getLastModified(): LocalDateTime? = runInterruptible {
        try {
            val lastModifiedMillis: Long = lastModified()
            Instant.ofEpochMilli(lastModifiedMillis)
                .atOffset(ZoneOffset.UTC)
                .toLocalDateTime()
        } catch (ignored: Exception) {
            null
        }
    }

    private companion object {

        private val PRIORITY_BY_SCHEME: Map<String, Int> = mapOf(
            "file" to 10,
            "war" to 5,
            "jar" to 3,
        )

        /**
         * Compares resources by the loading priority.
         * The most prior resources are from `filesystem`, following by `war`,
         * following by `jar`, and ending with `jar` inside `jar`.
         *
         * Returned values:
         * - positive integer if the first argument has greater priority;
         * - negative integer if the first argument has fewer priority;
         * - zero if arguments are equal.
         *
         * @return result of comparison of resources
         */
        private val RESOURCE_PRIORITY_COMPARATOR: Comparator<Resource> = compareBy(
            { PRIORITY_BY_SCHEME.getOrDefault(it.uri.scheme.lowercase(), -1) },
            { "BOOT-INF/lib" in it.uri.toString() },
        )
    }
}
