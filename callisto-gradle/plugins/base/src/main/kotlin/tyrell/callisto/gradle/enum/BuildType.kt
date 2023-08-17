package tyrell.callisto.gradle.enum

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
public enum class BuildType {

    SNAPSHOT,

    PATCH,

    RELEASE;

    companion object {

        @JvmStatic
        fun fromValue(value: Any): BuildType = valueOf(value.toString().uppercase())
    }
}
