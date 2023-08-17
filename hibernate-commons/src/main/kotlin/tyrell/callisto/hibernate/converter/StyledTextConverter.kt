package tyrell.callisto.hibernate.converter

import tyrell.callisto.base.definition.ExperimentalLibraryApi
import tyrell.callisto.base.text.vo.StyledText
import javax.persistence.Converter

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@ExperimentalLibraryApi
@Converter(autoApply = true)
public class StyledTextConverter : AbstractJsonConverter<StyledText>()
