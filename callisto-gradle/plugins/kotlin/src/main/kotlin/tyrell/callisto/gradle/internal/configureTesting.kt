package tyrell.callisto.gradle.internal

import kotlinx.kover.KoverPlugin
import kotlinx.kover.api.KoverTaskExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.plugins.JavaTestFixturesPlugin
import org.gradle.api.plugins.jvm.JvmTestSuite
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.withType
import org.gradle.testing.base.TestingExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import tyrell.callisto.gradle.internal.kotlin.extension.junitJupiterVersion
import tyrell.callisto.gradle.internal.kotlin.extension.kotestBundle
import tyrell.callisto.gradle.internal.kotlin.extension.kotlinTestingBundle
import tyrell.callisto.gradle.internal.kotlin.extension.singularVersion
import tyrell.callisto.gradle.internal.kotlin.extension.versionCatalog

@Suppress("UNUSED_PARAMETER")
internal fun Project.configureTesting(callisto: CallistoProject.Module) {
    apply<JavaTestFixturesPlugin>()
    apply<KoverPlugin>()

    tasks.named<KotlinCompile>("compileTestKotlin") {
        kotlinOptions {
            // TODO: `ExplicitApiMode.Disabled.toCompilerArg()` cannot be called
            //  as it produces `-Xexplicit-api=disabled` which is not supported
            //  what probably caused by bug in Gradle Kotlin DSL
            freeCompilerArgs += "-Xexplicit-api=disable"
        }
    }

    @Suppress("UnstableApiUsage")
    extensions.configure<TestingExtension> {
        // Configure the built-in test suite
        suites.withType<JvmTestSuite>().configureEach { testSuite ->
            // Use 'Kotlin Test' test framework
            // TODO: Uncomment when https://github.com/gradle/gradle/issues/19761 is resolved
            //  useKotlinTest(...) uses 'org.jetbrains.kotlin:kotlin-test-junit
            //  which is JUnit 4 binding (instead of JUnit 5)
            // testSuite.useKotlinTest(kotlinVersion.get().toString())
            testSuite.useJUnitJupiter(junitJupiterVersion.get())
            testSuite.dependencies { suiteDeps ->
                sequenceOf(kotlinTestingBundle, kotestBundle)
                    .flatMap { depsBundleProvider -> depsBundleProvider.get() }
                    .map { dependency -> "${dependency.module}:${dependency.versionConstraint.singularVersion}" }
                    .forEach { dependencyNotation -> suiteDeps.implementation(dependencyNotation) }
            }
        }
    }

    versionCatalog
        .flatMap { cat -> cat.findBundle("kotlin.testing").orElseThrow() }.get()
        .asSequence()
        .map { provider { it } }
        .forEach { dep: Provider<MinimalExternalModuleDependency> ->
            dependencies.add("testImplementation", dep)
        }
}
