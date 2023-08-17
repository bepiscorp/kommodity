package tyrell.callisto.data.l10n.service

import java.text.MessageFormat
import java.util.Locale

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
public class L10nTemplateDbServiceStub : L10nTemplateDbService {

    override fun getMessageFormat(templateCode: String, locale: Locale): MessageFormat? = null

    override fun getParameters(templateCode: String): List<String>? = null
}
