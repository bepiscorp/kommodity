package tyrell.callisto.data.typing

import com.fasterxml.jackson.annotation.JsonCreator
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import tyrell.callisto.base.definition.ExperimentalLibraryApi
import tyrell.callisto.base.definition.TransferredModel
import tyrell.callisto.base.messaging.model.DataRequest
import kotlin.math.max

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@ExperimentalLibraryApi
public class DataRequestDto @JsonCreator private constructor(

    override val page: Int,

    override val size: Int,

    override val filter: Map<String, Any>,

    private val sort: Sort,
) : DataRequest, Pageable, TransferredModel {

    override fun getPageNumber(): Int = page

    override fun getPageSize(): Int = size

    override fun getOffset(): Long = page.toLong() * size

    override fun getSort(): Sort = sort

    init {
        checkPage(page)
        checkSize(size)
    }

    override fun first(): DataRequestDto = copy { page = DataRequest.FIRST_PAGE_NUMBER }

    override fun next(): DataRequestDto = copy { page += 1 }

    override fun previousOrFirst(): DataRequestDto = copy { page = max(page - 1, DataRequest.FIRST_PAGE_NUMBER) }

    override fun withPage(pageNumber: Int): DataRequestDto = copy { page = pageNumber }

    override fun hasPrevious(): Boolean = (page > DataRequest.FIRST_PAGE_NUMBER)

    override fun copy(closure: DataRequest.BuilderScope.() -> Unit): DataRequestDto {
        val instance: DataRequestDto = this@DataRequestDto
        val scope = BuilderScope()

        scope.apply {
            page = instance.page
            size = instance.size
            filter = instance.filter
            sort = instance.sort

            closure(this)
        }

        return DataRequestDto(scope.page, scope.size, scope.filter, scope.sort)
    }

    private fun checkPage(page: Int) {
        check(page >= DataRequest.FIRST_PAGE_NUMBER) {
            "Illegal page number [$page]. Expected page number to be [${DataRequest.FIRST_PAGE_NUMBER}, ∞)"
        }
    }

    private fun checkSize(size: Int) {
        check(size >= DataRequest.FIRST_PAGE_NUMBER) {
            "Illegal page size [$size]. Expected page size to be a positive number"
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DataRequestDto) return false

        if (page != other.page) return false
        if (size != other.size) return false
        if (filter != other.filter) return false
        if (sort != other.sort) return false

        return true
    }

    override fun hashCode(): Int {
        var result = page
        result = 31 * result + size
        result = 31 * result + filter.hashCode()
        result = 31 * result + sort.hashCode()
        return result
    }

    public companion object {

        @JvmStatic
        public fun build(closure: BuilderScope.() -> Unit): DataRequestDto {
            val scope = BuilderScope().apply(closure)
            return DataRequestDto(scope.page, scope.size, scope.filter, scope.sort)
        }

        @JvmStatic
        public fun DataRequest.toDto(): DataRequest = build {
            val instance: DataRequest = this@toDto

            page = instance.page
            size = instance.size
            filter = instance.filter
            sort = instance.getSort()
        }
    }

    public open class BuilderScope internal constructor() : DataRequest.BuilderScope {

        override var page: Int = DataRequest.FIRST_PAGE_NUMBER

        override var size: Int = DataRequest.DEFAULT_PAGE_SIZE

        override var filter: Map<String, Any> = emptyMap()

        override var sort: Sort = Sort.unsorted()
    }
}
