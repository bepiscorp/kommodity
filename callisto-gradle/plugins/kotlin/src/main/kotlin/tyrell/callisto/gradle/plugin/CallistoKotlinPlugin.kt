package tyrell.callisto.gradle.plugin

import org.gradle.api.Project
import org.gradle.api.plugins.BasePlugin
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.repositories
import tyrell.callisto.gradle.enum.ProjectType
import tyrell.callisto.gradle.internal.AbstractCallistoPlugin
import tyrell.callisto.gradle.internal.CallistoProject
import tyrell.callisto.gradle.internal.configureCodeConventions
import tyrell.callisto.gradle.internal.configureDocumentation
import tyrell.callisto.gradle.internal.configureJava
import tyrell.callisto.gradle.internal.configureKotlin
import tyrell.callisto.gradle.internal.configureRootProjectDocumentation
import tyrell.callisto.gradle.internal.configureTesting
import tyrell.callisto.gradle.kotlin.dsl.projectType
import tyrell.callisto.gradle.kotlin.dsl.springReleaseDelegate

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
open class CallistoKotlinPlugin : AbstractCallistoPlugin() {

    override fun apply(target: Project) {
        super.apply(target)

        logger.debug("Applying Callisto Kotlin plugin to project [${project.name}]")
        with(project) {
            repositories {
                springReleaseDelegate()
            }

            when (val callisto = callisto) {
                is CallistoProject.Root -> applyToRootProject(callisto)
                is CallistoProject.Module -> applyToModuleProject(callisto)
                else -> Unit // Nothing to do
            }
        }
    }

    @Suppress("UNUSED_PARAMETER")
    private fun Project.applyToRootProject(callisto: CallistoProject.Root) {
        apply<BasePlugin>()
        configureRootProjectDocumentation(callisto)

        subprojects { subProject ->
            if (subProject.projectType == ProjectType.MODULE) {
                subProject.apply<CallistoKotlinPlugin>()
            }
        }
    }

    private fun Project.applyToModuleProject(callisto: CallistoProject.Module) {
        configureKotlin(callisto)
        configureJava(callisto)
        configureTesting(callisto)
        configureCodeConventions(callisto)
        configureDocumentation(callisto)
    }
}
