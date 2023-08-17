package tyrell.callisto.gradle.internal

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.ProjectConfigurationException
import org.gradle.kotlin.dsl.repositories
import org.gradle.util.GradleVersion
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import tyrell.callisto.gradle.internal.kotlin.extension.buildDirectory
import tyrell.callisto.gradle.kotlin.dsl.asCallistoProject
import tyrell.callisto.gradle.kotlin.dsl.gradlePluginPortalDelegate
import tyrell.callisto.gradle.kotlin.dsl.mavenCentralDelegate
import tyrell.callisto.gradle.kotlin.dsl.releasesRepo
import tyrell.callisto.gradle.kotlin.dsl.snapshotsRepo
import java.io.File

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
open class AbstractCallistoPlugin : Plugin<Project> {

    protected lateinit var project: Project
        private set

    protected lateinit var callisto: CallistoProject
        private set

    protected var logger: Logger = LoggerFactory.getLogger(this::class.java)
        private set

    override fun apply(target: Project) {
        this.project = target
        this.callisto = target.asCallistoProject()

        logger.debug("Applying [${this::class.simpleName}] to [$target]")
        checkGradleVersion(GradleVersion.current())

        with(project) {
            configureProject()
        }
    }

    private fun Project.configureProject() {
        repositories {
            mavenLocal()
            snapshotsRepo()
            releasesRepo()
            mavenCentralDelegate()
            gradlePluginPortalDelegate()
        }

        when (val callisto = callisto) {
            is CallistoProject.Root -> callisto.buildDirectory
            is CallistoProject.Module -> callisto.buildDirectory
            else -> null
        }?.let { buildDir = it.toFile() }
    }

    protected companion object {

        private const val REQUIRED_GRADLE_VERSION: String = "7.4"

        protected fun checkGradleVersion(current: GradleVersion) {
            if (current < GradleVersion.version(REQUIRED_GRADLE_VERSION)) {
                val file = File("gradle/wrapper/gradle-wrapper.properties")
                throw ProjectConfigurationException(
                    "Gradle version $REQUIRED_GRADLE_VERSION+ is required; the current version is $current." +
                        " Edit the distributionUrl in ${file.absolutePath}.",
                    IllegalStateException("Gradle version compliance failure")
                )
            }
        }
    }
}
