package tyrell.callisto.base.messaging.model

import com.fasterxml.jackson.annotation.JsonCreator
import tyrell.callisto.base.definition.LibraryApi
import tyrell.callisto.base.messaging.enums.ResponseType

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public data class ResponseCode @JsonCreator constructor(

    val code: String,

    val responseType: ResponseType,

    val messageCode: String,
)
