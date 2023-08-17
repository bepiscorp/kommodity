package tyrell.callisto.base.messaging.model

import tyrell.callisto.base.definition.LibraryApi
import tyrell.callisto.base.definition.TransferredModel
import java.io.Serializable

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public class LocalizableMessage : Serializable, TransferredModel {

    public val code: String

    public val parameters: List<Serializable?>

    public constructor(code: String, parameters: List<Any?>) {
        this.code = code
        this.parameters = if (parameters.isEmpty()) EMPTY_PARAMETERS else parameters.map { parameter ->
            when (parameter) {
                is Serializable -> parameter
                else -> parameter.toString() // TODO: Probably TypeUtils should be used here
            }
        }
    }

    public constructor(code: String, vararg parameters: Any?) : this(code, parameters.toList())

    public constructor(code: String) : this(code, emptyList())

    public fun withParameters(vararg parameters: Any?): LocalizableMessage =
        LocalizableMessage(code, *parameters)

    private companion object {

        private val EMPTY_PARAMETERS: List<Serializable?> = emptyList()
    }
}
