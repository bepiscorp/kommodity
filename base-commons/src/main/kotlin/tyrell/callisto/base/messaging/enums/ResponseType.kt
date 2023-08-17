package tyrell.callisto.base.messaging.enums

import tyrell.callisto.base.definition.LibraryApi

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public enum class ResponseType(
    public val dbKey: String,
    public val category: ResponseTypeCategory,
) {

    // region ================ Success response statuses ========================================

    /**
     * Success response type
     */
    SUCCESS("S", ResponseTypeCategory.SUCCESS),

    CREATED("CR", ResponseTypeCategory.SUCCESS),

    NONE("N", ResponseTypeCategory.SUCCESS),

    // endregion

    // region ================ Fail response statuses ========================================

    SERVICE_ERROR("SE", ResponseTypeCategory.FAIL),

    NOT_FOUND("NF", ResponseTypeCategory.FAIL),

    CONFLICT("CF", ResponseTypeCategory.FAIL),

    FORBIDDEN("FB", ResponseTypeCategory.FAIL),

    // endregion

    // region ================ Error response statuses ========================================

    TIMEOUT("T", ResponseTypeCategory.ERROR),

    ERROR("E", ResponseTypeCategory.ERROR),

    RECOVERABLE_ERROR("RE", ResponseTypeCategory.ERROR),

    SERVER_TIMEOUT("ST", ResponseTypeCategory.ERROR),

    CANCELLED("C", ResponseTypeCategory.ERROR);

    // endregion

    public companion object {

        private val valueByDbKey: Map<String, ResponseType> = values().associateBy { it.dbKey }

        @JvmStatic
        public fun fromDbKey(value: String): ResponseType = valueByDbKey.getValue(value)
    }
}
