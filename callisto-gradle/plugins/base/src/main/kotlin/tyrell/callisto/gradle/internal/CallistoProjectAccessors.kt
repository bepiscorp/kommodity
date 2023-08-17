package tyrell.callisto.gradle.internal

import net.swiftzer.semver.SemVer
import tyrell.callisto.gradle.CallistoProjectProperties
import tyrell.callisto.gradle.enum.BuildType
import tyrell.callisto.gradle.enum.ProjectType
import tyrell.callisto.gradle.internal.convention.ProjectCodeConvention
import tyrell.callisto.gradle.internal.convention.RootProjectNameFormatConvention
import tyrell.callisto.gradle.internal.kotlin.extension.globalBuildDirectory
import tyrell.callisto.gradle.internal.kotlin.extension.relativeProjectPath
import java.nio.file.Path
import kotlin.io.path.div
import kotlin.io.path.relativeTo

internal fun CallistoProject.resolveProjectVersion(): SemVer =
    resolveConfiguredVal<String?>(CallistoProjectProperties.PROJECT_VERSION) { project.version }
        ?.let(SemVer::parse)
        ?: error("Project version was not specified for project [$name]")

internal fun CallistoProject.resolveProjectCode(): String {
    RootProjectNameFormatConvention.verify(project)
    ProjectCodeConvention.verify(rootProject)

    return RootProjectNameRegex.extractProjectCode(rootProject.name)
}

internal fun CallistoProject.resolveProjectType(): ProjectType = when (this) {
    is CallistoProject.Container -> ProjectType.CONTAINER
    is CallistoProject.Root -> ProjectType.ROOT
    is CallistoProject.Module -> ProjectType.MODULE
}

internal fun CallistoProject.resolveBuildType(): BuildType =
    resolveConfiguredVal<String?>(CallistoProjectProperties.BUILD_TYPE)
        ?.let(BuildType::fromValue)
        ?: resolveProjectVersion().run {
            when {
                (preRelease == "SNAPSHOT") -> BuildType.SNAPSHOT
                (patch > 0) -> BuildType.PATCH
                else -> BuildType.RELEASE
            }
        }

internal fun CallistoProject.resolveGlobalBuildDirectory(): Path =
    resolveConfiguredVal(CallistoProjectProperties.GLOBAL_BUILD_DIR) { rootProject.projectDir / "build" }

internal fun CallistoProject.resolveBuildDirectory(): Path {
    val globalBuildDir = resolveGlobalBuildDirectory()
    return when (this) {
        is CallistoProject.Root -> globalBuildDir / name
        is CallistoProject.Module -> globalBuildDir / resolveRelativeProjectPath()!!
        is CallistoProject.Container -> globalBuildDir / resolveRelativeProjectPath()!!
    }
}

internal fun CallistoProject.resolveDistributionDirectory(): Path =
    resolveConfiguredVal(CallistoProjectProperties.DISTRIBUTION_DIR) { resolveGlobalBuildDirectory() / "distribution" }

internal fun CallistoProject.resolveReportsDirectory(): Path =
    resolveConfiguredPathOrNull(CallistoProjectProperties.REPORTS_DIR, normalize = false).let { configuredPath: Path? ->
        val projPath = relativeProjectPath ?: Path.of(name)
        when {
            (configuredPath == null) -> resolveBuildDirectory() / "reports"
            configuredPath.isAbsolute -> configuredPath / projPath
            else -> globalBuildDirectory / configuredPath / projPath
        }
    }

internal fun CallistoProject.resolveConfigDirectory(): Path = (rootProject.projectDir / "config")

internal fun CallistoProject.resolveDocumentationDirectory(): Path =
    resolveConfiguredVal(CallistoProjectProperties.DOCS_DIR) { rootProject.projectDir / "doc" }

internal fun CallistoProject.resolveRelativeProjectPath(): Path? {
    fun Path.isEmpty() = (nameCount > 0)
    fun Path.hasNonBlankRoot() = getName(0).toString().isNotBlank()

    val relativePath: Path = projectDir.relativeTo(rootProject.projectDir).normalize()
    return when {
        relativePath.isEmpty() && relativePath.hasNonBlankRoot() -> relativePath
        (this is CallistoProject.Root) -> null
        !relativePath.isEmpty() -> error("Illegal state: relative project path had no path elements")
        !relativePath.hasNonBlankRoot() -> error("Illegal state: Relative project path appeared to be empty")
        else -> error("Illegal state")
    }
}

/*internal*/ fun CallistoProject.resolvePublishingEnabled(): Boolean {
    if (CallistoProjectProperties.PUBLISHING !in this) return false
    val propertyValue: String? = this[CallistoProjectProperties.PUBLISHING]
    if (propertyValue.isNullOrBlank()) {
        return true
    }
    return propertyValue.lowercase().toBooleanStrict()
}

internal inline fun <reified T : Any?> CallistoProject.resolveConfiguredVal(
    propertyName: String,
    crossinline defaultBlock: (CallistoProject.() -> T?) = { null }
): T {
    val resolvedValue: T? = when (T::class) {
        Path::class -> resolveConfiguredPathOrNull(propertyName = propertyName)
        Boolean::class -> findProperty<String>(propertyName)?.toBoolean()
        String::class, Any::class -> findProperty<String>(propertyName)
        else -> error("Unsupported type type of required property [$propertyName]: [${T::class.qualifiedName}]")
    } as T?

    if (resolvedValue != null) {
        return resolvedValue
    }

    val nullableResult: Boolean = null is T
    val defaultValue: T? = defaultBlock()
    return when {
        (defaultValue != null) -> defaultValue
        nullableResult -> null as T
        else -> error("Required property [$propertyName] was not provided for project [$name]")
    }
}

internal fun CallistoProject.resolveConfiguredPathOrNull(propertyName: String, normalize: Boolean = true): Path? =
    findProperty<String>(propertyName)?.let(Path::of)
        ?.let { path -> if (normalize && !path.isAbsolute) rootProject.projectDir / path else path }
