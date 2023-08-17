package tyrell.callisto.gradle.internal

import tyrell.callisto.gradle.kotlin.dsl.asCallistoProject
import java.nio.file.Path

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
sealed class CallistoProject private constructor(
    @PublishedApi
    internal val gradleProject: GradleProject
) {

    val projectDir: Path = gradleProject.projectDir.toPath()

    val group: String get() = (gradleProject.group as String)

    val name: String get() = gradleProject.name

    val version: String? get() = (gradleProject.version as String).takeIf { it != GradleProject.DEFAULT_VERSION }

    abstract val project: CallistoProject

    abstract val rootProject: Root

    abstract val parentProject: CallistoProject?

    @Suppress("NOTHING_TO_INLINE")
    inline operator fun contains(propertyName: String): Boolean = hasProperty(propertyName)

    inline operator fun <reified T : Any> get(propertyName: String): T? = findProperty(propertyName)

    @Suppress("NOTHING_TO_INLINE")
    inline fun hasProperty(propertyName: String): Boolean = gradleProject.hasProperty(propertyName)

    inline fun <reified T : Any> findProperty(propertyName: String): T? {
        val propertyValue = gradleProject.findProperty(propertyName) ?: return null
        @Suppress("UNCHECKED_CAST")
        return propertyValue as T
    }

    inline fun <reified T : Any> property(propertyName: String): T? {
        val propertyValue = gradleProject.property(propertyName) ?: return null
        @Suppress("UNCHECKED_CAST")
        return propertyValue as T
    }

    internal companion object {

        internal fun fromGradleProject(project: GradleProject): CallistoProject = project.run {
            when {
                (this == rootProject) -> Root(this)
                hasSubprojects() && !hasBuildScript() -> Container(this)
                else -> Module(this)
            }
        }

        private fun GradleProject.hasBuildScript(): Boolean = projectDir.resolve("build.gradle.kts").exists()

        private fun GradleProject.hasSubprojects(): Boolean = subprojects.isNotEmpty()
    }

    class Root(gradleProject: GradleProject) : CallistoProject(gradleProject) {

        override val project: Root get() = this

        override val rootProject: Root get() = this

        override val parentProject: CallistoProject? get() = null
    }

    class Container(gradleProject: GradleProject) : CallistoProject(gradleProject) {

        override val project: Container get() = this

        // TODO: Analyze whether laziness can cause situations
        //  when project properties mutated and unexpected property value is resolved therefore
        override val rootProject: Root by lazy { gradleProject.rootProject.asCallistoProject() as Root }

        override val parentProject: CallistoProject by lazy { gradleProject.parent!!.asCallistoProject() }
    }

    class Module(gradleProject: GradleProject) : CallistoProject(gradleProject) {

        override val project: Module get() = this

        override val rootProject: Root by lazy { gradleProject.rootProject.asCallistoProject() as Root }

        override val parentProject: CallistoProject by lazy { gradleProject.parent!!.asCallistoProject() }
    }
}

internal typealias GradleProject = org.gradle.api.Project
