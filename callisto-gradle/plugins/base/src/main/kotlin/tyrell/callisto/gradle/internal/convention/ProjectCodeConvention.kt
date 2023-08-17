package tyrell.callisto.gradle.internal.convention

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toSet
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.runBlocking
import tyrell.callisto.gradle.internal.CallistoProject
import tyrell.callisto.gradle.internal.RootProjectNameRegex
import tyrell.callisto.gradle.internal.exception.RecoverableRepositoryException
import tyrell.callisto.gradle.internal.repository.SpaceIdentifiersRepository
import tyrell.callisto.gradle.internal.util.JupiterMoonsScrapper
import tyrell.callisto.gradle.internal.util.StarNamesScrapper

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 * @see JupiterMoonsScrapper
 * @see StarNamesScrapper
 */
public object ProjectCodeConvention : AbstractProjectConvention<CallistoProject>() {

    private const val jupyterMoonsListUrl: String = JupiterMoonsScrapper.sourceUrl

    private const val starNamesListUrl: String = StarNamesScrapper.sourceUrl

    private val spaceIdentifiersFlow: Flow<String> = buildSpaceIdentifiersFlow()

    override fun VerificationResultBuilder.doVerify(project: CallistoProject) {
        val rootProject: CallistoProject.Root = project.rootProject
        val projectCode = RootProjectNameRegex.extractProjectCode(rootProject.name)

        val allowedProjectCodes: Set<String> = try {
            loadAllowedProjectCodes()
        } catch (ex: RecoverableRepositoryException) {
            warning("Could not read allowed project codes: ${ex.message}")
            return
        }

        errorIf(
            { projectCode !in allowedProjectCodes },
            buildString {
                append(
                    "Code [$projectCode] of root project [${rootProject.name}]",
                    " MUST be name of Jupiter moon or Star name."
                )
                appendLine()

                appendLine("Please, rename the root project to much this convention.")
                appendLine()

                appendLine("Jupiter moons: $jupyterMoonsListUrl")
                appendLine("Star names: $starNamesListUrl")
            }
        )
    }

    private fun loadAllowedProjectCodes(): Set<String> = runBlocking { spaceIdentifiersFlow.toSet() }

    private fun buildSpaceIdentifiersFlow() = flow {
        with(SpaceIdentifiersRepository) {
            emitAll(loadOrGetJupiterMoons().asFlow())
            emitAll(loadOrGetStarNames().asFlow())
        }
    }.transform { spaceIdentifier ->
        emit(spaceIdentifier)
        emit(spaceIdentifier.lowercase())
    }
}
