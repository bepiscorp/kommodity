package tyrell.callisto.gradle.internal

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByName
import org.gradle.kotlin.dsl.withType

private const val DEFAULT_ENCODING = "UTF-8"

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
abstract class AbstractCallistoJavaPlugin : AbstractCallistoPlugin() {

    abstract fun applyImplementation()

    override fun apply(target: Project) {
        super.apply(target)

        applyImplementation()

        if (project.plugins.hasPlugin(JavaPlugin::class.java)) {
            with(project.extensions.getByName<JavaPluginExtension>("java")) {
                sourceCompatibility = JavaVersion.VERSION_11
                targetCompatibility = JavaVersion.VERSION_11

                sourceSets["main"].compileClasspath += project.configurations.maybeCreate("provided")
            }
        }

        project.tasks.withType<JavaCompile> {
            options.encoding = DEFAULT_ENCODING
            val isTestTask = name.contains("Test")
            if (!isTestTask) {
                options.compilerArgs.add("-Xlint:unchecked")
                options.compilerArgs.add("-Xlint:deprecation")
            }
            doFirst { removeDuplicateCompilerArgs() }
        }
    }
}

/**
 * Removes duplicate `-Xlint:opt` parameters from a `javac` task.
 * If something is both disabled and enabled, make the disable win.
 *
 * Examples:
 *  * given: `-Xlint:unchecked -Xlint:-deprecation -Xlint:deprecation`,
 *    result: `-Xlint:unchecked -Xlint:-deprecation`
 *  * given: `-Xlint:unchecked -Xlint:deprecation -Xlint:-deprecation`,
 *    result: `-Xlint:unchecked -Xlint:-deprecation`
 *
 * Usage:
 * ```
 * tasks.withType(JavaCompile).configureEach { doFirst { removeDuplicateCompilerArgs(it) } }
 * ```
 *
 * Since the suggested usage is [org.gradle.api.Task.doFirst],
 * it doesn't matter if it's before or after the relevant `options.compilerArgs += [ ... ]` setup.
 */
private fun JavaCompile.removeDuplicateCompilerArgs() {
    logger.debug("$this (input): ${options.compilerArgs}")
    val duplicates = options.compilerArgs
        .filter { it.startsWith("-Xlint:-") }
        .map { "-Xlint:${it.substring("-Xlint:-".length)}" }
    options.compilerArgs.removeAll(duplicates)
    logger.debug("$this (filtered): ${options.compilerArgs}")
}
