package tyrell.callisto.base.text.vo

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData
import tyrell.callisto.base.definition.ExperimentalLibraryApi
import tyrell.callisto.base.definition.TransferredModel
import tyrell.callisto.base.text.model.CaseStyle
import tyrell.callisto.base.text.model.MarkupStyle

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@ExperimentalLibraryApi
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class StyledText(

    @JacksonXmlCData
    public val text: String,

    public val markup: MarkupStyle? = null,

    public val case: CaseStyle? = null,
) : TransferredModel, Comparable<StyledText> {

    override fun compareTo(other: StyledText): Int = COMPARATOR.compare(this, other)

    private companion object {

        private val COMPARATOR: Comparator<StyledText> = compareBy({ it.text }, { it.markup }, { it.case })
    }
}
