package tyrell.callisto.base.messaging.enums

import tyrell.callisto.base.definition.LibraryApi

/**
 * Response statuses according to [JSend JSON Response Specification](https://github.com/omniti-labs/jsend)
 *
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public enum class ResponseTypeCategory(public val code: String) {

    /**
     * All went well, and (usually) some data was returned.
     */
    SUCCESS("success"),

    /**
     * There was a problem with the data submitted, or some pre-condition of the API call wasn't satisfied.
     */
    FAIL("fail"),

    /**
     * An error occurred in processing the request, i.e. an exception was thrown.
     */
    ERROR("error");

    public companion object {

        private val valueByCode: Map<String, ResponseTypeCategory> = values().associateBy { it.code }

        @JvmStatic
        public fun fromCode(value: String): ResponseTypeCategory = valueByCode.getValue(value)
    }
}
