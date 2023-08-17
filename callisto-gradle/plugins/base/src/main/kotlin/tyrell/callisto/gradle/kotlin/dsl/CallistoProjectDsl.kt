@file:Suppress("unused")

package tyrell.callisto.gradle.kotlin.dsl

import java.nio.file.Path
import net.swiftzer.semver.SemVer
import org.gradle.api.Project
import tyrell.callisto.gradle.enum.BuildType
import tyrell.callisto.gradle.enum.ProjectType
import tyrell.callisto.gradle.internal.CallistoProject
import tyrell.callisto.gradle.internal.kotlin.extension.buildType
import tyrell.callisto.gradle.internal.kotlin.extension.configDirectory
import tyrell.callisto.gradle.internal.kotlin.extension.distributionDirectory
import tyrell.callisto.gradle.internal.kotlin.extension.documentationDirectory
import tyrell.callisto.gradle.internal.kotlin.extension.globalBuildDirectory
import tyrell.callisto.gradle.internal.kotlin.extension.projectCode
import tyrell.callisto.gradle.internal.kotlin.extension.projectType
import tyrell.callisto.gradle.internal.kotlin.extension.projectVersion
import tyrell.callisto.gradle.internal.kotlin.extension.relativeProjectPath
import tyrell.callisto.gradle.internal.kotlin.extension.reportsDirectory

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
val Project.projectVersion: SemVer get() = asCallistoProject().projectVersion

/**
 * Returns code of this project.
 * Project code is recognized by name of the root project, so each subproject will have the same project code.
 * Root project **MUST** much regex defined in [tyrell.callisto.gradle.internal.RootProjectNameRegex].
 *
 * @author Mikhail Gostev
 * @since 0.0.1
 */
val Project.projectCode: String get() = asCallistoProject().projectCode

/**
 * Returns type of this project.
 * [ProjectType.ROOT] means this project is root project
 * [ProjectType.CONTAINER] means this project only contains is not the root one and
 *
 * @author Mikhail Gostev
 * @since 0.0.1
 */
val Project.projectType: ProjectType get() = asCallistoProject().projectType

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
val Project.buildType: BuildType get() = asCallistoProject().buildType

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
val Project.globalBuildDirectory: Path get() = asCallistoProject().globalBuildDirectory

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
val Project.distributionDirectory: Path get() = asCallistoProject().distributionDirectory

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
val Project.reportsDirectory: Path get() = asCallistoProject().rootProject.reportsDirectory

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
val Project.configDirectory: Path get() = asCallistoProject().configDirectory

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
val Project.documentationDirectory: Path get() = asCallistoProject().documentationDirectory

/**
 * Returns path of [Project] relatively to the [Project.getRootProject].
 * If [Project] is a root project then `null` is returned.
 *
 * @author Mikhail Gostev
 * @since 0.0.1
 */
val Project.relativeProjectPath: Path? get() = asCallistoProject().relativeProjectPath

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
/*internal*/ fun Project.asCallistoProject(): CallistoProject = CallistoProject.fromGradleProject(this)
