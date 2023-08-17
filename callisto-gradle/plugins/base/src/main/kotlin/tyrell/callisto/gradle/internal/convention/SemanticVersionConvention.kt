package tyrell.callisto.gradle.internal.convention

import net.swiftzer.semver.SemVer
import tyrell.callisto.gradle.internal.CallistoProject
import tyrell.callisto.gradle.internal.kotlin.extension.relativeProjectPath
import tyrell.callisto.gradle.internal.resolveProjectVersion

/**
 * Checks project version vs [Semantic Versioning 2.0.0](https://semver.org/spec/v2.0.0.html) specification (SemVer).
 *
 * Given a version number `MAJOR.MINOR.PATCH`, increment the:
 * 1. `MAJOR` version when you make incompatible API changes;
 * 2. `MINOR` version when you add functionality in a backwards compatible manner;
 * 3. `PATCH` version when you make backwards compatible bug fixes.
 * Additional labels for pre-release and build metadata are available as extensions to the MAJOR.MINOR.PATCH format.
 *
 * @author Mikhail Gostev
 * @since 0.0.1
 * @see SemVer
 */
public object SemanticVersionConvention : AbstractProjectConvention<CallistoProject>() {

    private const val semverSpecificationUrl: String = "https://semver.org/spec/v2.0.0.html"

    override fun VerificationResultBuilder.doVerify(project: CallistoProject) {
        errorIf(
            { runCatching { project.resolveProjectVersion() }.isFailure },
            buildString {
                val projectDescription = project.relativeProjectPath ?: project.name
                append("Version [${project.version}] of project [$projectDescription]")
                    .appendLine(" MUST much Semantic Versioning specification.")
                appendLine("Please, define project version matching this convention.")
                    .appendLine()
                appendLine("Specification of Semantic Versioning 2.0.0: $semverSpecificationUrl")
            }
        )
    }
}
