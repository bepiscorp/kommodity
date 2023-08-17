package tyrell.callisto.data.util

import tyrell.callisto.base.l10n.LocalizerParameterDefinition

internal object LocalizerUtils {

    // The part of regex [\w\s/,.#] seems to be matching the parameter name
    // the exact reasons for choosing this pattern are lost
    // This pattern is used to match named parameters
    private val NAMED_PARAM_MATCH_PATTERN: Regex = Regex("(?:^|[^\\\\])\\$\\{([\\w\\s/,.#]+)}")

    // This pattern is used to extract list of named parameters and replace with {index}
    private val NAMED_PARAM_EXTRACT_PATTERN: Regex = Regex("\\$\\{([\\w\\s/,.#]+)}")

    // This pattern is used to get parameter's name
    private val PARAM_NAME_PATTERN: Regex = Regex("[\\w/.]+")

    /**
     * Returns set of named parameters from message template
     *
     * @param templateText template text
     *
     * @return list of [LocalizerParameterDefinition]
     */
    fun extractParameters(templateText: String): List<LocalizerParameterDefinition> {
        val codes: MutableList<String> = mutableListOf()
        val params: MutableList<LocalizerParameterDefinition> = mutableListOf()

        val matches = NAMED_PARAM_EXTRACT_PATTERN.findAll(templateText)
        for (match in matches) {
            val parameterCode = getParameterCode(match.groupValues[1], templateText)
            if (parameterCode !in codes) {
                codes += parameterCode
            }

            val index = codes.indexOf(parameterCode)
            params += LocalizerParameterDefinition(
                code = parameterCode,
                index = index,
            )
        }

        return params
    }

    private fun getParameterCode(charsBetweenBrackets: String, templateText: String): String =
        PARAM_NAME_PATTERN.find(charsBetweenBrackets)?.let { it.groupValues[0] } ?: error(
            "Parameter [$charsBetweenBrackets] could not be parsed for template text [$templateText]",
        )
}
