package tyrell.callisto.gradle.internal.convention

import tyrell.callisto.gradle.internal.CallistoProject
import tyrell.callisto.gradle.internal.RootProjectNameRegex

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 * @see RootProjectNameRegex
 */
public object RootProjectNameFormatConvention : AbstractProjectConvention<CallistoProject>() {

    override fun VerificationResultBuilder.doVerify(project: CallistoProject) {
        val rootProject: CallistoProject.Root = project.rootProject
        errorIf(
            { !RootProjectNameRegex.matches(rootProject.name) },
            """
                Name of root project [${rootProject.name}] MUST match regex [${RootProjectNameRegex.INSTANCE}].
                Please, rename the root project to much this convention.
            """.trimIndent()
        )
    }
}
