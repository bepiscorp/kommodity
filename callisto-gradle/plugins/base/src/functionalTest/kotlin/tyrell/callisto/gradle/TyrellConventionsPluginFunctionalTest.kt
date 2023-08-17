package tyrell.callisto.gradle

import java.io.File
import kotlin.test.Ignore
import kotlin.test.Test
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner

@Ignore
class TyrellConventionsPluginFunctionalTest {

    @Test
    fun `can run task`() {
        // Set up the test build
        val projectDir = File("build/functionalTest").apply {
            mkdirs()

            resolve("settings.gradle.kts").writeText("")

            resolve("build.gradle.kts").writeText(
                """
                    plugins {
                        id("tyrell.callisto.base")
                    }
                """.trimIndent()
            )
        }

        // Run the build
        val runner: GradleRunner = GradleRunner.create().apply {
            forwardOutput()
            withPluginClasspath()
            withProjectDir(projectDir)
        }

        val result: BuildResult = runner.build()

        // TODO: Do some testing
    }
}
