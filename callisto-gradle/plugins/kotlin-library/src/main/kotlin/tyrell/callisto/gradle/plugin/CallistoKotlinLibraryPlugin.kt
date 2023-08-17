package tyrell.callisto.gradle.plugin

import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.repositories
import tyrell.callisto.gradle.enum.ProjectType
import tyrell.callisto.gradle.internal.CallistoProject
import tyrell.callisto.gradle.internal.configureKotlinLibrary
import tyrell.callisto.gradle.internal.configurePublishing
import tyrell.callisto.gradle.internal.configureRootProjectPublishing
import tyrell.callisto.gradle.internal.kotlin.extension.publishingEnabled
import tyrell.callisto.gradle.kotlin.dsl.projectType
import tyrell.callisto.gradle.kotlin.dsl.springReleaseDelegate

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@Suppress("unused")
class CallistoKotlinLibraryPlugin : CallistoKotlinPlugin() {

    override fun apply(target: Project) {
        super.apply(target)

        logger.debug("Applying Callisto Kotlin Library plugin to project [${project.name}]")
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
        if (callisto.publishingEnabled) {
            configureRootProjectPublishing(callisto)
        }

        subprojects { subProject ->
            if (subProject.projectType == ProjectType.MODULE) {
                subProject.apply<CallistoKotlinLibraryPlugin>()
            }
        }
    }

    private fun Project.applyToModuleProject(callisto: CallistoProject.Module) {
        configureKotlinLibrary(callisto)

        if (callisto.publishingEnabled) {
            configurePublishing(callisto)
        }
    }
}
