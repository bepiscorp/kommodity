package tyrell.callisto.data.enum

import tyrell.callisto.base.definition.LibraryApi

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public enum class AuditState(override val dbKey: String) : DbEnum {

    /**
     * Prototype state of resource.
     * Used to express such a state when resource is not ready to be used in system but already exists.
     *
     * @since 0.6.0
     */
    PROTOTYPE("P"),

    /**
     * Active state of resource.
     */
    ACTIVE("A"),

    /**
     * Deactivated state.
     * Used to express such a state when resource is deactivated but must not be removed from database.
     *
     * @since 0.6.0
     */
    DEACTIVATED("D"),

    /**
     * Removed state.
     * Used to express such a resource that is marked for deletion.
     */
    REMOVED("R")
}
