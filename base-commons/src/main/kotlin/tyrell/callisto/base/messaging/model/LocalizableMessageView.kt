package tyrell.callisto.base.messaging.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import tyrell.callisto.base.definition.ExperimentalLibraryApi
import tyrell.callisto.base.definition.TransferredModel
import java.io.Serializable

public fun LocalizableMessage.asView(): LocalizableMessageView = LocalizableMessageView(code, parameters)

/**
 * The copy of [LocalizableMessage] having additional fields (e.g. human readable message representation).
 *
 * Localizable message view is to be sent in [ResponseData].
 *
 * @author Mikhail Gostev
 * @since 0.2.0
 */
@ExperimentalLibraryApi
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LocalizableMessageView : Serializable, TransferredModel {

    public val code: String

    public val parameters: List<Serializable?>

    public val humanReadableMessage: String?

    public constructor(
        code: String,
        parameters: List<Any?>,
        humanReadableMessage: String?,
    ) {
        this.code = code
        this.parameters = if (parameters.isEmpty()) EMPTY_PARAMETERS else parameters.map { parameter ->
            when (parameter) {
                is Serializable -> parameter
                else -> parameter.toString() // TODO: Probably TypeUtils should be used here
            }
        }
        this.humanReadableMessage = humanReadableMessage
    }

    public constructor(code: String, vararg parameters: Any?) :
        this(code, parameters.toList(), humanReadableMessage = null)

    public constructor(code: String) : this(code, emptyArray<Any?>())

    public fun withParameters(vararg parameters: Any?): LocalizableMessageView =
        LocalizableMessageView(code, *parameters)

    public fun asLocalizableMessage(): LocalizableMessage = LocalizableMessage(code, parameters)

    private companion object {

        private val EMPTY_PARAMETERS: List<Serializable?> = emptyList()
    }
}
