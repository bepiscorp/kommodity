package tyrell.callisto.base.resource.vo

import tyrell.callisto.base.definition.LibraryApi
import tyrell.callisto.base.kotlin.dsl.uncheckedCast
import java.time.LocalDateTime

/**
 * VO class representing loaded resource as:
 * - full path to loaded resource [fullResourcePath]
 * - resource content [loadedObject]
 *
 * @param V resource content type
 *
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public class LoadedResource<out V : Any> private constructor(
    public val value: V,
    public val identifier: String,
    public val schema: String,
    public val fullResourcePath: String?,
    public val lastModified: LocalDateTime?,
) {

    public fun <N : Any> copy(closure: BuilderScope<N>.() -> Unit): LoadedResource<N> {
        val instance: LoadedResource<V> = this
        val scope = BuilderScope<N>().apply {
            value = uncheckedCast(instance.value)
            identifier = instance.identifier
            schema = instance.schema
            fullResourcePath = instance.fullResourcePath
            lastModified = instance.lastModified

            closure(this)
        }

        return LoadedResource(
            value = scope.value,
            identifier = scope.identifier,
            fullResourcePath = scope.fullResourcePath,
            schema = scope.schema,
            lastModified = scope.lastModified,
        )
    }

    public companion object {

        public fun <N : Any> build(closure: BuilderScope<N>.() -> Unit): LoadedResource<N> {
            val scope = BuilderScope<N>().apply(closure)
            return LoadedResource(
                value = scope.value,
                identifier = scope.identifier,
                schema = scope.schema,
                fullResourcePath = scope.fullResourcePath ?: scope.identifier,
                lastModified = scope.lastModified,
            )
        }
    }

    public class BuilderScope<T : Any> internal constructor() {

        public lateinit var schema: String

        public lateinit var identifier: String

        public var fullResourcePath: String? = null

        public var lastModified: LocalDateTime? = null

        public lateinit var value: T
    }
}
