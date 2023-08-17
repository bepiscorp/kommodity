/**
 * @author Mikhail Gostev
 */
package tyrell.callisto.base.kotlin.extension

import tyrell.callisto.base.StandardResponseCode
import tyrell.callisto.base.definition.LibraryApi
import tyrell.callisto.base.exception.ServiceException

/**
 * Retrieves an unique value from a collection — the collection must have exactly one element.
 *
 * @param descriptionSupplier supplier of description of the entity to be retrieved, used in case of errors
 * @return first element of the collection if it contains one element
 * @throws ServiceException if the collection contains no elements or more than one element
 *
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public inline fun <T> Collection<T>.retrieveUniqueResult(crossinline descriptionSupplier: () -> String): T = when {
    this.isEmpty() -> throw ServiceException(StandardResponseCode.STD0012_NO_DATA_FOUND, descriptionSupplier())
    this.size > 1 -> throw ServiceException(
        StandardResponseCode.STD0013_UNEXPECTED_MULTIPLE_DATA,
        descriptionSupplier(),
    )
    else -> this.first()
}

/**
 * Retrieves an optional value from a collection — the collection must have either one or zero elements.
 *
 * @param descriptionSupplier supplier of description of the entity to be retrieved, used in case of errors
 *
 * @return `null` if collection contains no elements, first element if collection contains one element
 *
 * @throws ServiceException if the collection contains more than one element
 */
@LibraryApi
public inline fun <T> Collection<T>.retrieveOptionalResult(crossinline descriptionSupplier: () -> String): T? = when {
    this.isEmpty() -> null
    this.size > 1 -> throw ServiceException(
        StandardResponseCode.STD0013_UNEXPECTED_MULTIPLE_DATA,
        descriptionSupplier(),
    )
    else -> this.first()
}
