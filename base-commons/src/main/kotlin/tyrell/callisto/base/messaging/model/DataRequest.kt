package tyrell.callisto.base.messaging.model

import org.springframework.data.domain.Sort
import tyrell.callisto.base.definition.ExperimentalLibraryApi

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@ExperimentalLibraryApi
public interface DataRequest {

    public val page: Int

    public val size: Int

    public val filter: Map<String, Any>

    public fun getSort(): Sort

    public fun copy(closure: BuilderScope.() -> Unit): DataRequest

    public interface BuilderScope {

        public var page: Int

        public var size: Int

        public var filter: Map<String, Any>

        public var sort: Sort
    }

    public companion object {

        public const val FIRST_PAGE_NUMBER: Int = 1

        public const val DEFAULT_PAGE_SIZE: Int = 20
    }
}
