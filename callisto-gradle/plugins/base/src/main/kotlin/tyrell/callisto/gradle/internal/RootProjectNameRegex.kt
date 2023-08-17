package tyrell.callisto.gradle.internal

internal object RootProjectNameRegex {

    /**
     * Name of group in [Regex] instance that contains unit name
     * @see CallistoProject.unitName]
     */
    private const val GROUP_PROJECT_CODE: String = "projectCode"

    /**
     * [Regex] that root project must match
     */
    internal val INSTANCE: Regex = "^(?<$GROUP_PROJECT_CODE>.+?)-parent$".toRegex()

    fun extractProjectCode(value: String): String = INSTANCE.matchEntire(value)!!.groups[GROUP_PROJECT_CODE]!!.value

    fun matches(value: String): Boolean = value.matches(INSTANCE)
}
