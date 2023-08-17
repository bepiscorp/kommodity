package tyrell.callisto.data.l10n.service

import tyrell.callisto.base.definition.LibraryApi
import java.text.MessageFormat
import java.util.Locale

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public interface L10nTemplateDbService {

    /**
     * @param templateCode template code
     * @param locale desired message locale
     *
     * @return [MessageFormat] corresponding to given template code and locale
     */
    public fun getMessageFormat(templateCode: String, locale: Locale): MessageFormat?

    /**
     * Returns list of parameter names used in template.
     * The parameters should be logged in the order specified by the returned list.
     *
     * @param templateCode template code
     *
     * @return ordered list of parameter names
     */
    public fun getParameters(templateCode: String): List<String>?
}
