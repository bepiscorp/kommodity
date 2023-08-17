package tyrell.callisto.data.l10n

import mu.KLogger
import tyrell.callisto.base.definition.InternalLibraryApi
import tyrell.callisto.base.kotlin.properties.LoggingDelegates
import tyrell.callisto.base.l10n.Localizer
import tyrell.callisto.base.l10n.TextTemplate
import tyrell.callisto.base.messaging.model.LocalizableMessage
import tyrell.callisto.data.l10n.service.L10nTemplateDbService
import tyrell.callisto.data.l10n.service.L10nTemplateDbServiceStub
import tyrell.callisto.data.util.LocalizerUtils
import java.text.MessageFormat
import java.util.Locale
import java.util.SortedSet
import java.util.concurrent.ConcurrentHashMap

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@InternalLibraryApi
public class LocalizerImpl : Localizer {

    private val messageFormats: MutableMap<Locale, MutableMap<String, MessageFormat>> = ConcurrentHashMap()

    private val namedParams: MutableMap<String, MutableList<String>> = ConcurrentHashMap()

    private val l10nTemplateService: L10nTemplateDbService

    public constructor(l10nTemplateService: L10nTemplateDbService) {
        this.l10nTemplateService = l10nTemplateService
    }

    public constructor() : this(L10nTemplateDbServiceStub())

    override fun addTemplates(locale: Locale, newTemplates: Map<String, TextTemplate>) {
        val messageFormatByCode = messageFormats.computeIfAbsent(locale) { ConcurrentHashMap() }
        newTemplates.asSequence()
            .map(::processNamedParams)
            .forEach { (key, value) ->
                val localFormat = MessageFormat(value.text, locale)
                for ((argumentIndex, newFormat) in value.args) {
                    localFormat.setFormatByArgumentIndex(argumentIndex, newFormat)
                }
                messageFormatByCode[key] = localFormat
            }
    }

    override fun localize(locale: Locale, message: LocalizableMessage): String {
        TODO("Not yet implemented")
    }

    override fun localize(locale: Locale, messageCode: String): String = localize(locale, messageCode, messageCode)

    override fun localize(locale: Locale, messageCode: String, defaultMessage: String): String {
        val messageFormat: MessageFormat? = getLocalMessageFormat(locale, messageCode)
        return if (messageFormat == null) {
            LOGGER.trace { "Unknown template code has been requested: $messageCode." }
            defaultMessage
        } else {
            messageFormat.format(null)
        }
    }

    override fun getSupportedLocales(): SortedSet<Locale> =
        messageFormats.keys.toSortedSet(LOCALE_SIMILARITY_COMPARATOR)

    override fun getSupportedCodes(locale: Locale, codePrefixes: Collection<String>): Set<String> {
        if (!isLocaleSupported(locale)) {
            return emptySet()
        }

        val localFormats = messageFormats.getValue(locale)
        return if (codePrefixes.isEmpty()) {
            localFormats.keys
        } else {
            localFormats.keys.asSequence()
                .filter { code -> codePrefixes.any { code.startsWith(it) } }
                .toSet()
        }
    }

    override fun isLocaleSupported(locale: Locale): Boolean = (locale in messageFormats)

    override fun isLocaleSupported(locale: Locale, code: String): Boolean =
        isLocaleSupported(locale) && (code in messageFormats.getValue(locale))

    /**
     * Process named parameters in message template
     *
     * @param entry message entry
     *
     * @return modified message entry
     */
    private fun processNamedParams(entry: Map.Entry<String, TextTemplate>): Pair<String, TextTemplate> {
        val templateCode: String = entry.key
        val templateText: String = entry.value.text

        val existingParams = namedParams.computeIfAbsent(templateCode) { mutableListOf() }
        addNamedParams(templateText, existingParams)

        val textTemplate = TextTemplate(replaceParamsWithIndexes(templateText, existingParams))
        return Pair(entry.key, textTemplate)
    }

    override fun getParameters(templateCode: String): List<String> {
        val params = l10nTemplateService.getParameters(templateCode)
        if (params != null) {
            return params
        }
        return if (namedParams.containsKey(templateCode)) {
            namedParams[templateCode]!!
        } else emptyList()
    }

    /**
     * Extracts named parameters from template text and recalculate their indexes based on existing parameters
     *
     * @param templateText   template text
     * @param existingParams set of existing parameters
     */
    private fun addNamedParams(
        templateText: String,
        existingParams: MutableList<String>,
    ) {
        val newParams = LocalizerUtils.extractParameters(templateText)
        for (param in newParams) {
            val code = param.code
            if (code !in existingParams) {
                existingParams += code
            }
        }
    }

    /**
     * Replaces named params in template with the corresponding indexes
     *
     * @param templateText   template text
     * @param existingParams set of existing parameters
     *
     * @return template text with indexes instead of named parameters
     */
    private fun replaceParamsWithIndexes(
        templateText: String,
        existingParams: List<String>,
    ): String {
        // TODO: Add support of named parameters with replacing name parameters with ordinal placeholders: {0}, {1}, ...
        return templateText
    }

    /**
     * Retrieves formatter for specified locale and message code.
     *
     * @param locale Locale to be used for translation
     * @param code   Message code
     *
     * @return `null` if locale or message code is missing, otherwise cached instance of [MessageFormat]
     */
    private fun getLocalMessageFormat(locale: Locale, code: String): MessageFormat? {
        val serviceMessageFormat = l10nTemplateService.getMessageFormat(code, locale)
        if (serviceMessageFormat != null) {
            return serviceMessageFormat
        }

        val formats = messageFormats[locale] ?: emptyMap()
        return formats[code]
    }

    /**
     * Returns if code start with any prefix from prefix list or of prefix list is empty.
     *
     * @param code message code
     * @param codePrefixes list if message code prefix for filtering
     *
     * @return if code start with any prefix from prefix list.
     */
    private fun filterCodeByPrefixes(
        code: String,
        codePrefixes: List<String>,
    ): Boolean = codePrefixes.any { prefix -> code.startsWith(prefix) }

    private companion object {

        private val LOGGER: KLogger by LoggingDelegates.classLogger(LocalizerImpl::class)

        private val LOCALE_SIMILARITY_COMPARATOR: Comparator<Locale> = compareBy<Locale>(
            { computeLocaleSimilarity(it, Locale.getDefault()) },
            { computeLocaleSimilarity(it, Locale.US) },
        ).reversed()

        private fun computeLocaleSimilarity(locale: Locale, example: Locale): Int {
            var similarity = 0
            if (example.language == locale.language) similarity += 2
            if (example.country == locale.country) similarity += 1
            return similarity
        }
    }
}
