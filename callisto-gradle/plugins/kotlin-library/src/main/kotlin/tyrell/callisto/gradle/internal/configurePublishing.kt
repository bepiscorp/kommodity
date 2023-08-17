package tyrell.callisto.gradle.internal

import groovy.util.Node
import groovy.util.NodeList
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.PasswordCredentials
import org.gradle.api.plugins.BasePluginExtension
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.api.publish.plugins.PublishingPlugin
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.the
import org.gradle.plugins.signing.SigningExtension
import org.gradle.plugins.signing.SigningPlugin
import tyrell.callisto.gradle.internal.kotlin.extension.publishingEnabled
import tyrell.callisto.gradle.kotlin.dsl.buildType

@Suppress("UNUSED_PARAMETER")
internal fun Project.configureRootProjectPublishing(callisto: CallistoProject.Root) {
    check(callisto.publishingEnabled) { "Publication was tried to be configured on disabled publication" }
    apply<PublishingPlugin>()
}

@Suppress("UNUSED_PARAMETER")
internal fun Project.configurePublishing(callisto: CallistoProject.Module) {
    check(callisto.publishingEnabled) { "Publication was tried to be configured on disabled publication" }

    apply<MavenPublishPlugin>()
    apply<SigningPlugin>()

    configurePublishing()
    configureSigning()
}

private fun Project.configurePublishing() = configure<PublishingExtension> {
    repositories { handler: RepositoryHandler ->
        handler.mavenLocal()

        // TODO: Add publishing into remote corporate repo
        // when (buildType) {
        //     BuildType.SNAPSHOT -> {
        //         handler.snapshotsRepo { credentials(PasswordCredentials::class.java) }
        //     }
        //     BuildType.PATCH, BuildType.RELEASE -> {
        //         handler.releasesRepo { credentials(PasswordCredentials::class.java) }
        //     }
        //     else -> error("Unsupported build type: [$buildType]")
        // }
    }

    publications.register<MavenPublication>(project.buildType.name.lowercase()) {
        setupArtifacts(project)
        setupModuleIdentity(project)
        // reorderNodes(project) TODO: Investigate reason why build fails when try to reorder
    }
}

private fun Project.configureSigning() = configure<SigningExtension> {
    // -PsigningKeyId to gradlew, or ORG_GRADLE_PROJECT_signingKeyId env var
    val signingKeyId: String? by project // Gradle 6+ only
    // -PsigningKey to gradlew, or ORG_GRADLE_PROJECT_signingKey env var
    val signingKey: String? by project
    // -PsigningPassword to gradlew, or ORG_GRADLE_PROJECT_signingPassword env var
    val signingPassword: String? by project
    if (signingKey != null && signingPassword != null) {
        useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
        sign(project.the<PublishingExtension>().publications["release"])
    }
}

private fun MavenPublication.setupModuleIdentity(project: Project) = project.afterEvaluate {
    groupId = project.group as String
    artifactId = project.the<BasePluginExtension>().archivesName.getOrElse(project.name)
    version = project.version as String

    pom {
        it.name.set(project.name)
        it.description.set(project.description)
    }
}

private fun MavenPublication.setupArtifacts(project: Project) {
    from(project.components["java"])
    suppressPomMetadataWarningsFor("testFixturesApiElements")
    suppressPomMetadataWarningsFor("testFixturesRuntimeElements")
}

private fun MavenPublication.reorderNodes(project: Project) = project.afterEvaluate {
    fun Node.getChildren(localName: String): NodeList = this.get(localName) as NodeList
    fun NodeList.nodes(): List<Node> = (this as Iterable<*>).filterIsInstance<Node>()
    fun Node.getChild(localName: String): Node = this.getChildren(localName).nodes().single()

    pom.withXml { xmlProvider ->
        xmlProvider.asNode().apply {
            val lastNodes = listOf(
                getChild("modelVersion"),
                getChild("groupId"),
                getChild("artifactId"),
                getChild("version"),
                getChild("name"),
                getChild("description"),
                getChild("url"),
                getChild("dependencies"),
                getChild("scm"),
                getChild("developers")
            )
            lastNodes.forEach { remove(it) }
            lastNodes.forEach { append(it) }
        }
    }
}
