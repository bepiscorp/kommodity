package tyrell.callisto.gradle.internal

import com.diffplug.gradle.spotless.KotlinExtension
import com.diffplug.gradle.spotless.SpotlessExtension
import com.diffplug.gradle.spotless.SpotlessPlugin
import com.diffplug.spotless.LineEnding
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import io.gitlab.arturbosch.detekt.DetektGenerateConfigTask
import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import tyrell.callisto.gradle.internal.kotlin.extension.javaVersion
import tyrell.callisto.gradle.internal.kotlin.extension.ktlintVersion
import tyrell.callisto.gradle.internal.kotlin.extension.reportsDirectory
import java.nio.charset.Charset

fun Project.configureCodeConventions(callisto: CallistoProject.Module) {
    configureSpotless(callisto)
    configureDetekt(callisto)
}

@Suppress("UNUSED_PARAMETER")
private fun Project.configureSpotless(callisto: CallistoProject.Module) {
    apply<SpotlessPlugin>()
    extensions.configure<SpotlessExtension> {
        kotlin { ext: KotlinExtension ->
            ext.apply {
                target("**/*.kt")
                ktlint(ktlintVersion.get()).editorConfigOverride(ktlintEditorConfig)
                lineEndings = LineEnding.UNIX
                encoding = Charset.forName("UTF-8")
                trimTrailingWhitespace()
                endWithNewline()
                indentWithSpaces(4)
            }
        }
    }
}

private fun Project.configureDetekt(callisto: CallistoProject.Module) {
    apply<DetektPlugin>()
    extensions.configure<DetektExtension> {
        // The directories where detekt looks for source files.
        // Defaults to `files("src/main/java", "src/test/java", "src/main/kotlin", "src/test/kotlin")`.
        source = files("src/main/java", "src/main/kotlin")

        // Builds the AST in parallel. Rules are always executed in parallel.
        // Can lead to speedups in larger projects. `false` by default.
        parallel = true

        // TODO: Consider configuring
        // Define the detekt configuration(s) you want to use.
        // Defaults to the default detekt configuration.
        // config = files("path/to/config.yml")

        // TODO: Consider configuring
        // Applies the config files on top of detekt's default config file. `false` by default.
        // buildUponDefaultConfig = false

        // TODO: Consider configuring
        // Specifying a baseline file. All findings stored in this file in subsequent runs of detekt.
        // baseline = file("path/to/baseline.xml")

        // If set to `true` the build does not fail when the
        // maxIssues count was reached. Defaults to `false`.
        ignoreFailures = true

        reportsDir = callisto.reportsDirectory.toFile()
    }
    tasks.withType<Detekt>().configureEach { task ->
        task.jvmTarget = javaVersion.get().toString()
        with(task.reports) {
            html.required.set(true)
            txt.required.set(true)
        }
    }
    tasks.withType<DetektCreateBaselineTask>().configureEach { task ->
        task.jvmTarget = javaVersion.get().toString()
    }
    @Suppress("UNUSED_ANONYMOUS_PARAMETER")
    tasks.withType<DetektGenerateConfigTask>().configureEach { task ->
        // Nothing to do
    }
}
