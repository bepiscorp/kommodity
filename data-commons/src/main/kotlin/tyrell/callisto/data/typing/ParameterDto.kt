package tyrell.callisto.data.typing

import com.fasterxml.jackson.annotation.JsonAlias
import tyrell.callisto.base.definition.TransferredModel
import tyrell.callisto.base.type.Parameter

public data class ParameterDto(

    @JsonAlias("name") // for backward compatibility
    override val code: String,

    override val value: Any? = null,

) : Parameter<Any?>, TransferredModel
