package tyrell.callisto.gradle.plugin

import org.gradle.api.Project
import org.gradle.api.plugins.BasePlugin
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.plugins.ide.idea.IdeaPlugin
import org.gradle.plugins.ide.idea.model.IdeaModel
import tyrell.callisto.gradle.internal.AbstractCallistoPlugin
import tyrell.callisto.gradle.internal.CallistoProject

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@Suppress("unused")
class CallistoBasePlugin : AbstractCallistoPlugin() {

    override fun apply(target: Project) {
        super.apply(target)

        project.apply<BasePlugin>()
        with(project) {
            when (callisto) {
                is CallistoProject.Root -> applyToRootProject()
                else -> Unit // Nothing to do
            }
        }
    }

    private fun Project.applyToRootProject() {
        @Suppress("UNUSED_VARIABLE")
        val callisto = callisto as CallistoProject.Root

        for (subproject in project.subprojects) {
            subproject.apply<CallistoBasePlugin>()
        }

        apply<IdeaPlugin>()
        extensions.configure<IdeaModel> {
            module {
                it.isDownloadJavadoc = true
                it.isDownloadSources = true
            }
        }
    }
}
