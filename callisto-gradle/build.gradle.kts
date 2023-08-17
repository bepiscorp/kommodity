import org.gradle.kotlin.dsl.provider.KotlinDslPluginSupport
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.time.Instant
import java.time.format.DateTimeFormatter

allprojects {
    // Extension with name 'libs' does not exist. Currently registered extension names: [ext, kotlin, kotlinTestRegistry, base, defaultArtifacts, sourceSets, reporting, java, javaToolchains, testing]
    // Needs to be called different from libs,
    // because com.android.tools.idea.gradle.dsl.model.ext.PropertyUtil.followElement
    // from idea-2021.1.3\plugins\android-gradle-dsl\lib\android-gradle-dsl-impl.jar
    // runs into an infinite loop on it.
    // TODEL Affects anything with Android Plugin < 2020.3.1 (i.e. AS 4.x, and IJ <2021.3)
    val deps = rootProject.libs
    replaceGradlePluginAutoDependenciesWithoutKotlin()

    configurations.all {
        replaceKotlinJre7WithJdk7()
        replaceKotlinJre8WithJdk8()
        resolutionStrategy {
            // Make sure we don't have many versions of Kotlin lying around.
            force(deps.kotlin.stdlib)
            force(deps.kotlin.reflect)
            force(deps.kotlin.stdlib.jdk8)
        }
    }

    plugins.withId("kotlin") {
        tasks.withType<KotlinCompile>() {
            kotlinOptions {
                val kotlinVersion = libs.versions.kotlin.get()
                    .split('.').map { it.toInt() }
                    .let { (major, minor, _) -> "$major.$minor" }

                apiVersion = kotlinVersion
                languageVersion = kotlinVersion

                freeCompilerArgs += KotlinDslPluginSupport.kotlinCompilerArgs
                jvmTarget = libs.versions.java.get()
            }
        }

        dependencies {
            addProvider<MinimalExternalModuleDependency, ExternalModuleDependency>(
                "compileOnly", deps.gradle.plugins.kotlin.gradleDsl
            ) { isTransitive = false } // make sure to not pull in kotlin-compiler-embeddable
            add("api", deps.kotlin.stdlib)
            add("api", deps.kotlin.stdlib.jdk8)
            add("api", deps.kotlin.reflect)

            add("testImplementation", deps.kotlin.test)
        }
    }

    plugins.withId("java") {
        extensions.configure<JavaPluginExtension> {
            toolchain {
                vendor.set(JvmVendorSpec.ADOPTIUM)
                languageVersion.set(JavaLanguageVersion.of(deps.versions.java.get()))
            }
            // withJavadocJar()
            // withSourcesJar()
        }
        tasks.named<Test>("test") { testLogging.events("passed", "skipped", "failed") }
        afterEvaluate {
            tasks.named<Jar>("jar") {
                manifest {
                    attributes(
                        mapOf(
                            // Implementation-* used by TestPlugin
                            "Implementation-Vendor" to project.group,
                            "Implementation-Title" to project.base.archivesName.get(),
                            "Implementation-Version" to project.version,
                            "Built-Date" to if ((project.version as String).endsWith("-SNAPSHOT"))
                                DateTimeFormatter.ISO_INSTANT.format(Instant.ofEpochMilli(0))
                            else
                                DateTimeFormatter.ISO_INSTANT.format(Instant.now())
                        )
                    )
                }
            }
        }
    }
}
