package tyrell.callisto.base.type

import tyrell.callisto.base.definition.LibraryApi
import java.time.Clock

/**
 * @author Mikhail Gostev
 * @since 0.1.0
 */
@LibraryApi
public interface ClockAware {

    public var clock: Clock
}
