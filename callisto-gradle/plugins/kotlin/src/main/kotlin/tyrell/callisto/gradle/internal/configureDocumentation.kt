package tyrell.callisto.gradle.internal

import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.named
import org.jetbrains.dokka.gradle.DokkaMultiModuleTask
import org.jetbrains.dokka.gradle.DokkaPlugin
import tyrell.callisto.gradle.kotlin.dsl.documentationDirectory

@Suppress("UNUSED_PARAMETER")
internal fun Project.configureRootProjectDocumentation(callisto: CallistoProject.Root) {
    apply<DokkaPlugin>()
    afterEvaluate {
        if ("dokkaHtmlMultiModule" in tasks.names) { // Task can be absent on root project without children
            tasks.named<DokkaMultiModuleTask>("dokkaHtmlMultiModule").configure { task ->
                task.outputDirectory.set(documentationDirectory.resolve("dokka").toFile())
            }
        }
    }
}

@Suppress("UNUSED_PARAMETER")
internal fun Project.configureDocumentation(callisto: CallistoProject.Module) {
    configureDokka(callisto)
}

@Suppress("UNUSED_PARAMETER")
private fun Project.configureDokka(callisto: CallistoProject.Module) {
    apply<DokkaPlugin>()
}
