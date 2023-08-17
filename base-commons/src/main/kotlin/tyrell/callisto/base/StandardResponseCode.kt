package tyrell.callisto.base

import tyrell.callisto.base.definition.LibraryApi
import tyrell.callisto.base.messaging.enums.ResponseType
import tyrell.callisto.base.messaging.enums.ResponseType.CANCELLED
import tyrell.callisto.base.messaging.enums.ResponseType.ERROR
import tyrell.callisto.base.messaging.enums.ResponseType.NONE
import tyrell.callisto.base.messaging.enums.ResponseType.SERVICE_ERROR
import tyrell.callisto.base.messaging.enums.ResponseType.SUCCESS
import tyrell.callisto.base.messaging.model.ResponseCode

/**
 * The [StandardResponseCode] defines core response codes constants.
 *
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public object StandardResponseCode {

    /**
     * None response code.
     */
    @JvmField public val STD0000_NONE: ResponseCode = ResponseCode(
        code = "STD-0000", responseType = NONE,
        messageCode = "std.msg.response.std0000_none",
    )

    /**
     * Processing successfully completed.
     */
    @JvmField public val STD0001_SUCCESS: ResponseCode = ResponseCode(
        code = "STD-0001", responseType = SUCCESS,
        messageCode = "std.msg.response.std0001_success",
    )

    /**
     * Generic message processing failure.
     */
    @JvmField public val STD0002_SERVICE_ERROR: ResponseCode = ResponseCode(
        code = "STD-0002", responseType = SERVICE_ERROR,
        messageCode = "std.msg.response.std0002_service_error",
    )

    /**
     * Message processing unexpected error.
     */
    @JvmField public val STD0003_ERROR: ResponseCode = ResponseCode(
        code = "STD-0003", responseType = ERROR,
        messageCode = "std.msg.response.std0003_error",
    )

    /**
     * Message delivery error.
     */
    @JvmField public val STD0004_DELIVERY_ERROR: ResponseCode = ResponseCode(
        code = "STD-0004", responseType = ERROR,
        messageCode = "std.msg.response.std0004_delivery_error",
    )

    /**
     * Processing timeout.
     */
    @JvmField public val STD0005_TIMEOUT: ResponseCode = ResponseCode(
        code = "STD-0005", responseType = ResponseType.TIMEOUT,
        messageCode = "std.msg.response.std0005_timeout",
    )

    /**
     * Processing has been interrupted.
     */
    @JvmField public val STD0006_INTERRUPTED: ResponseCode = ResponseCode(
        code = "STD-0006", responseType = CANCELLED,
        messageCode = "std.msg.response.std0006_interrupted",
    )

    /**
     * Request is not supported.
     */
    @JvmField public val STD0007_UNSUPPORTED: ResponseCode = ResponseCode(
        code = "STD-0007", responseType = ERROR,
        messageCode = "std.msg.response.std0007_unsupported",
    )

    /**
     * Request was recognized as a duplicate.
     */
    @JvmField public val STD0008_DUPLICATE: ResponseCode = ResponseCode(
        code = "STD-0008", responseType = SERVICE_ERROR,
        messageCode = "std.msg.response.std0008_duplicate",
    )

    /**
     * Destination for message is not available.
     */
    @JvmField public val STD0009_NOT_AVAILABLE: ResponseCode = ResponseCode(
        code = "STD-0009", responseType = ERROR,
        messageCode = "std.msg.response.std0009_not_available",
    )

    /**
     * Request processing not possible due to policy validation reasons.
     */
    @JvmField public val STD0010_POLICY: ResponseCode = ResponseCode(
        code = "STD-0010", responseType = ERROR,
        messageCode = "std.msg.response.std0010_policy",
    )

    /**
     * Message processing not possible due server-side data validation.
     *
     * Parameters provided to message for localization:
     * 1. Entity/Object name
     * 2. Detailed error message
     */
    @JvmField public val STD0011_VALIDATION: ResponseCode = ResponseCode(
        code = "STD-0011", responseType = SERVICE_ERROR,
        messageCode = "std.msg.response.std0011_validation",
    )

    /**
     * The request data has not been found.
     *
     * Parameters provided to message for localization:
     * 1. Entity/Object name
     */
    @JvmField public val STD0012_NO_DATA_FOUND: ResponseCode = ResponseCode(
        code = "STD-0012", responseType = SERVICE_ERROR,
        messageCode = "std.msg.response.std0012_no_data_found",
    )

    /**
     * Multiple results have been found, but unique has been expected.
     *
     * Parameters provided to message for localization:
     * 1. Entity/Object name
     */
    @JvmField public val STD0013_UNEXPECTED_MULTIPLE_DATA: ResponseCode = ResponseCode(
        code = "STD-0013", responseType = SERVICE_ERROR,
        messageCode = "std.msg.response.std0013_unexpected_multiple_data",
    )
}
