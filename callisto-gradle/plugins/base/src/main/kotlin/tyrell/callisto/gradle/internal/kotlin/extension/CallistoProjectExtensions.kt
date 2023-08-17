package tyrell.callisto.gradle.internal.kotlin.extension

import net.swiftzer.semver.SemVer
import tyrell.callisto.gradle.enum.BuildType
import tyrell.callisto.gradle.enum.ProjectType
import tyrell.callisto.gradle.internal.CallistoProject
import tyrell.callisto.gradle.internal.resolveBuildDirectory
import tyrell.callisto.gradle.internal.resolveBuildType
import tyrell.callisto.gradle.internal.resolveConfigDirectory
import tyrell.callisto.gradle.internal.resolveDistributionDirectory
import tyrell.callisto.gradle.internal.resolveDocumentationDirectory
import tyrell.callisto.gradle.internal.resolveGlobalBuildDirectory
import tyrell.callisto.gradle.internal.resolveProjectCode
import tyrell.callisto.gradle.internal.resolveProjectType
import tyrell.callisto.gradle.internal.resolveProjectVersion
import tyrell.callisto.gradle.internal.resolvePublishingEnabled
import tyrell.callisto.gradle.internal.resolveRelativeProjectPath
import tyrell.callisto.gradle.internal.resolveReportsDirectory
import java.nio.file.Path

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
val CallistoProject.projectVersion: SemVer get() = resolveProjectVersion()

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
val CallistoProject.projectCode: String get() = resolveProjectCode()

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
val CallistoProject.projectType: ProjectType get() = resolveProjectType()

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
val CallistoProject.buildType: BuildType get() = resolveBuildType()

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
val CallistoProject.globalBuildDirectory: Path get() = resolveGlobalBuildDirectory()

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
val CallistoProject.Root.buildDirectory: Path get() = resolveBuildDirectory()

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
val CallistoProject.Module.buildDirectory: Path get() = resolveBuildDirectory()

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
val CallistoProject.distributionDirectory: Path get() = resolveDistributionDirectory()

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
val CallistoProject.reportsDirectory: Path get() = resolveReportsDirectory()

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
val CallistoProject.configDirectory: Path get() = resolveConfigDirectory()

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
val CallistoProject.documentationDirectory: Path get() = resolveDocumentationDirectory()

/**
 * Returns path of [CallistoProject] relatively to the root project.
 * If [CallistoProject] is a root project then `null` is returned.
 *
 * @author Mikhail Gostev
 * @since 0.0.1
 */
val CallistoProject.relativeProjectPath: Path? get() = resolveRelativeProjectPath()

/**
 * Returns `true` if publishing configuration is enabled for current build.
 *
 * @author Mikhail Gostev
 * @since 0.0.1
 */
val CallistoProject.publishingEnabled: Boolean get() = resolvePublishingEnabled()
