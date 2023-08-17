package tyrell.callisto.gradle.internal

import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.kotlin.dsl.the
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode
import org.jetbrains.kotlin.gradle.dsl.KotlinCommonOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinCompile as BaseKotlinCompile

@Suppress("UNUSED_PARAMETER")
internal fun Project.configureKotlinLibrary(callisto: CallistoProject.Module) {
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
private fun Project.configureKotlinJvm(compileTask: KotlinCompile, extension: KotlinJvmProjectExtension) {
    // Nothing to do
}

@Suppress("unused")
private fun Project.applyGeneralTaskConfig(
    compileTask: BaseKotlinCompile<KotlinCommonOptions>
): BaseKotlinCompile<KotlinCommonOptions> = compileTask.apply {
    // Nothing to do
}

private fun Project.applyGeneralExtensionConfig(
    extension: KotlinProjectExtension
) = extension.apply {
    explicitApi = ExplicitApiMode.Strict
}
