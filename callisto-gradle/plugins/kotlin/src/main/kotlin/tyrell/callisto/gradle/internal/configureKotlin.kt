package tyrell.callisto.gradle.internal

import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.jvm.toolchain.JavaToolchainSpec
import org.gradle.jvm.toolchain.JvmVendorSpec
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.the
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinCommonOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformJvmPlugin
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import tyrell.callisto.gradle.CallistoDefinitions
import tyrell.callisto.gradle.internal.kotlin.extension.javaVersion
import tyrell.callisto.gradle.internal.kotlin.extension.kotlinVersion
import org.jetbrains.kotlin.gradle.dsl.KotlinCompile as BaseKotlinCompile

@Suppress("UNUSED_PARAMETER")
internal fun Project.configureKotlin(callisto: CallistoProject.Module) {
    logger.debug("Configuring Kotlin")
    plugins.apply("kotlin")
    apply<KotlinPlatformJvmPlugin>()

    val kotlinCompileTaskByName = tasks
        .withType<BaseKotlinCompile<KotlinCommonOptions>>()
        .associateBy { it.name }

    if (kotlinCompileTaskByName.isEmpty()) {
        throw GradleException(
            """
                Error happened during applying Kotlin convention from Callisto plugin plugin.
                Could not find any task of type [KotlinCompile] in tasks list.
            """.trimIndent()
        )
    }
    if ("compileKotlin" !in kotlinCompileTaskByName) {
        throw GradleException(
            """
                Error happened during applying Kotlin convention from Callisto plugin plugin.
                Could not find task with name [compileKotlin] in tasks list.
            """.trimIndent()
        )
    }

    // Apply common options for all [KotlinCompile] tasks: 'compileKotlin', 'compileTest', ...
    val kotlinExtensionByName: Map<String, KotlinProjectExtension> = kotlinCompileTaskByName.mapValues { (_, task) ->
        when (task) {
            is KotlinCompile -> the<KotlinJvmProjectExtension>()
            else -> error("Unsupported task type: [${task::class.java}]")
        }
    }

    for (taskName in kotlinCompileTaskByName.keys) {
        val task = applyGeneralTaskConfig(kotlinCompileTaskByName.getValue(taskName))
        val extension = applyGeneralExtensionConfig(kotlinExtensionByName.getValue(taskName))
        when (task) {
            is KotlinCompile -> configureKotlinJvm(task, extension as KotlinJvmProjectExtension)
        }
    }
}

@Suppress("UNUSED_PARAMETER")
internal fun Project.configureKotlinJvm(compileTask: KotlinCompile, extension: KotlinJvmProjectExtension) {
    compileTask.apply {
        kotlinOptions {
            freeCompilerArgs = (freeCompilerArgs + listOf("-Xjsr305=strict")).also {
                logger.debug(
                    """
                        Applied compiler args for [$name] task of project [${project.name}]:
                        ${it.joinToString(separator = " ")}
                    """.trimIndent()
                )
            }

            jvmTarget = javaVersion.get().toString()
        }
    }

    dependencies {
        add("implementation", kotlin("stdlib-jdk8"))
    }
}

private fun Project.applyGeneralTaskConfig(
    compileTask: BaseKotlinCompile<KotlinCommonOptions>
): BaseKotlinCompile<KotlinCommonOptions> = compileTask.apply {
    kotlinOptions {
        val majorMinor = kotlinVersion.get().run { "$major.$minor" }
        apiVersion = majorMinor
        languageVersion = majorMinor
    }

    // Additionally configure 'compileKotlin' task
    if (name == "compileKotlin") {
        kotlinOptions {
            suppressWarnings = false

            freeCompilerArgs = buildList<String> {
                this += freeCompilerArgs
                this += CallistoDefinitions.optInApiAnnotationsQualifiers.map { "-Xopt-in=$it" }
                this += "-Xopt-in=kotlin.RequiresOptIn"
            }.distinct()
        }
    }

    dependencies {
        val kotlinVersion = kotlinVersion.get().toString()
        add("implementation", kotlin("stdlib", kotlinVersion))
        add("implementation", kotlin("reflect", kotlinVersion))
    }
}

private fun Project.applyGeneralExtensionConfig(
    extension: KotlinProjectExtension
) = extension.apply {
    coreLibrariesVersion = kotlinVersion.get().toString()
    jvmToolchain { spec ->
        (spec as JavaToolchainSpec).run {
            vendor.set(JvmVendorSpec.ADOPTIUM)
            languageVersion.set(JavaLanguageVersion.of(javaVersion.get().toString()))
        }
    }
}
