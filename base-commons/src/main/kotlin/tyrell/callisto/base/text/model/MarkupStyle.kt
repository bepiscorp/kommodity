package tyrell.callisto.base.text.model

import tyrell.callisto.base.definition.ExperimentalLibraryApi
import tyrell.callisto.base.definition.InternalLibraryApi

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@ExperimentalLibraryApi
public enum class MarkupStyle {

    PLAIN_TEXT,

    MARKDOWN,

    @InternalLibraryApi
    ASCII_DOC,

    @InternalLibraryApi
    HTML,

    @InternalLibraryApi
    LATEX;
}
