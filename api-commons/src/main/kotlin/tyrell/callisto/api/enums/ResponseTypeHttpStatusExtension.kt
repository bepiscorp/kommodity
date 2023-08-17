package tyrell.callisto.api.enums

import org.springframework.http.HttpStatus
import tyrell.callisto.base.definition.LibraryApi
import tyrell.callisto.base.messaging.enums.ResponseType

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public val ResponseType.httpStatus: HttpStatus
    get() = when (this) {
        ResponseType.SUCCESS -> HttpStatus.OK
        ResponseType.CREATED -> HttpStatus.CREATED
        ResponseType.SERVICE_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR
        ResponseType.NOT_FOUND -> HttpStatus.NOT_FOUND
        ResponseType.CONFLICT -> HttpStatus.CONFLICT
        ResponseType.FORBIDDEN -> HttpStatus.FORBIDDEN
        ResponseType.TIMEOUT -> HttpStatus.REQUEST_TIMEOUT
        ResponseType.ERROR -> HttpStatus.INTERNAL_SERVER_ERROR
        ResponseType.RECOVERABLE_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR
        ResponseType.SERVER_TIMEOUT -> HttpStatus.GATEWAY_TIMEOUT
        ResponseType.CANCELLED -> HttpStatus.INTERNAL_SERVER_ERROR
        ResponseType.NONE -> error("Response type [NONE] cannot be considered as HTTP response status")
    }
