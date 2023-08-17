package tyrell.callisto.signal.model

import tyrell.callisto.base.definition.LibraryApi
import java.time.LocalDateTime

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public interface Signal {

    public val type: String

    public val timestamp: LocalDateTime
}
