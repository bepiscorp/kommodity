package tyrell.callisto.base.text.model

import tyrell.callisto.base.definition.ExperimentalLibraryApi

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@ExperimentalLibraryApi
public enum class CaseStyle(public val example: String) {

    CAPITALIZED("Capitalized Words"),

    UPPER_CASE_WORDS("UPPER CASE"),

    SNAKE_CASE("snake_case"),

    SCREAMING_SNAKE_CASE("SCREAMING_SNAKE_CASE"),

    KEBAB_CASE("kebab-case"),

    CAMEL_CASE("camelCase");
}
