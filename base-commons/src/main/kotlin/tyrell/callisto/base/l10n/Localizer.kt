package tyrell.callisto.base.l10n

import tyrell.callisto.base.definition.LibraryApi
import tyrell.callisto.base.messaging.model.LocalizableMessage
import java.util.Locale
import java.util.SortedSet

/**
 * [Localizer] stores templates for formatting and localizing messages to different languages.
 * It also provides means to localize [LocalizableMessage] to particular locale.
 *
 * @author Mikhail Gostev
 * @since 0.0.1
 * @see java.text.MessageFormat
 */
@LibraryApi
public interface Localizer {

    /**
     * Adds templates for particular locale.
     * This method is thread-safe, it is secure to run it in parallels even for one locale.
     *
     * @param locale       Templates' locale
     * @param newTemplates [Map] with localization templates. `Key` is template code, `value` is template
     */
    public fun addTemplates(locale: Locale, newTemplates: Map<String, TextTemplate>)

    /**
     * Translates message to specified locale. [java.text.MessageFormat] is used for formatting.
     * Any parameter of [message] that is instance of [LocalizableMessage] will be localized recursively.
     *
     * Any parameter of [message] that is instance of [Collection]
     * will be converted to a string by concatenating its elements using ", " delimiter.
     *
     * @param locale  Locale to be used for translation
     * @param message Message to be translated
     * @return formatted message if formatter was found, message code and list of arguments otherwise
     */
    public fun localize(locale: Locale, message: LocalizableMessage): String

    /**
     * Retrieves translation for code (message without parameters)
     *
     * @param locale      Locale to be used for translation
     * @param messageCode Code of template to be retrieved
     *
     * @return template text for locale if it was found, message code otherwise
     */
    public fun localize(locale: Locale, messageCode: String): String

    /**
     * Retrieves translation for code (message without parameters)
     *
     * @param locale         Locale to be used for translation
     * @param messageCode    Code of template to be retrieved
     * @param defaultMessage Default text to be returned if template with specified code is not present
     *
     * @return template text for locale if it was found, [defaultMessage] otherwise
     */
    public fun localize(locale: Locale, messageCode: String, defaultMessage: String): String

    /**
     * Returns [Set] of supported locales.
     * Locale is considered to be supported if there are any templates for this locale.
     *
     * @return set of supported locales
     */
    public fun getSupportedLocales(): SortedSet<Locale>

    /**
     * Returns `true` if locale is supported.
     * Locale is considered to be supported if there are any templates for this locale.
     *
     * @param locale locale
     *
     * @return `true` if locale is supported, `false` otherwise
     */
    public fun isLocaleSupported(locale: Locale): Boolean

    /**
     * Returns `true` if locale is supported for particular template code.
     * Locale is considered to be supported if it has template for specified code.
     *
     * @param locale locale
     * @param code template code
     *
     * @return `true` if locale is supported for template code, `false` otherwise
     */
    public fun isLocaleSupported(locale: Locale, code: String): Boolean

    /**
     * Returns [Set] of message codes for specified locale.
     * Message codes are filtered to satisfy prefixes from [codePrefixes].
     * If [codePrefixes] is empty, the returned codes are not filtered.
     *
     * @param locale locale
     * @param codePrefixes list of message code prefixes for filtering
     *
     * @return set of message codes
     */
    public fun getSupportedCodes(locale: Locale, codePrefixes: Collection<String> = emptyList()): Set<String>

    /**
     * Returns [List] of named parameters used in template.
     * The parameters should be specified in the order specified by the returned list.
     *
     * @param templateCode template code
     *
     * @return ordered list of parameter names
     */
    public fun getParameters(templateCode: String): List<String>
}
