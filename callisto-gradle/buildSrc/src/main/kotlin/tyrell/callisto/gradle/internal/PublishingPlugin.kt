package tyrell.callisto.gradle.internal

import base
import groovy.util.Node
import groovy.util.NodeList
import java
import kotlin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.BasePluginExtension
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.TaskProvider
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.the
import org.gradle.plugin.devel.GradlePluginDevelopmentExtension
import org.gradle.plugins.signing.SigningExtension
import org.jetbrains.dokka.gradle.DokkaTask

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
class PublishingPlugin : Plugin<Project> {

    private lateinit var project: Project

    private val dokkaJavadoc: TaskProvider<DokkaTask> by lazy { configureDokkaJavadocTask() }

    private val sourcesJar: TaskProvider<Jar> by lazy { registerSourcesJarTask() }

    private val javadocJar: TaskProvider<Jar> by lazy { registerJavadocJarTask(dokkaJavadoc) }

    override fun apply(target: Project) {
        this.project = target

        with(project) {
            plugins.apply {
                apply("maven-publish")
                apply("signing")
                apply("org.jetbrains.dokka")
                withId("java-gradle-plugin") {
                    configure<GradlePluginDevelopmentExtension> {
                        // https://github.com/gradle/gradle/issues/11611
                        isAutomatedPublishing = false
                    }
                }
            }

            artifacts.add("archives", sourcesJar)
            artifacts.add("archives", javadocJar)

            configurePublishing()
            configureSigning()
        }
    }

    private fun Project.configurePublishing() = configure<PublishingExtension> {
        configurePublishingRepositories()

        val releasePublication: Boolean = repositories.none { it.name.contains("snapshot", ignoreCase = true) }
        publications.register<MavenPublication>(if (releasePublication) "release" else "snapshot") {
            setupModuleIdentity()
            setupArtifacts()
            configurePublishedPom()
            reorderNodes()
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

    private fun MavenPublication.setupModuleIdentity() = project.afterEvaluate {
        groupId = group as String
        artifactId = the<BasePluginExtension>().archivesName.getOrElse(project.name)
        version = version as String

        pom {
            val compoundDescription: String? = project.description?.takeIf { ':' in it }
            checkNotNull(compoundDescription) {
                "[$project] must have a description with format: \"Module Display Name: Module description.\""
            }

            val (pomName, pomDescription) = compoundDescription.split(':')
                .onEach { check(it.isNotBlank()) }
                .map { it.trim() }

            name.set(pomName)
            description.set(pomDescription)
        }
    }

    private fun MavenPublication.setupArtifacts(): Unit = with(project) {
        from(components["java"])
        suppressPomMetadataWarningsFor("testFixturesApiElements")
        suppressPomMetadataWarningsFor("testFixturesRuntimeElements")

        artifact(sourcesJar) { classifier = "sources" }
        artifact(javadocJar) { classifier = "javadoc" }
    }

    private fun MavenPublication.configurePublishedPom() {
        @Suppress("UnstableApiUsage")
        pom {
            url.set("https://phabricator.cybernation.com/source/callisto")
            scm {
                connection.set("scm:git:phabricator.cybernation.com:2222/source/callisto.git")
                developerConnection.set("scm:git:ssh://git@phabricator.cybernation.com:2222/source/callisto.git")
                url.set("https://phabricator.cybernation.com/source/callisto/history/develop")
            }
            developers {
                developer {
                    id.set("gostev.mikhail")
                    name.set("Mikhail Gostev")
                    email.set("mikhail.gostev@arrival.com")
                }
            }
        }
    }

    private fun MavenPublication.reorderNodes() = project.afterEvaluate {
        fun Node.getChildren(localName: String): NodeList = this.get(localName) as NodeList
        fun NodeList.nodes(): List<Node> = (this as Iterable<*>).filterIsInstance<Node>()
        fun Node.getChild(localName: String): Node = this.getChildren(localName).nodes().single()

        pom.withXml {
            asNode().apply {
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

    private fun configureDokkaJavadocTask(): TaskProvider<DokkaTask> =
        project.tasks.named<DokkaTask>("dokkaJavadoc") {
            // TODO https://github.com/Kotlin/dokka/issues/1894
            moduleName.set(this.project.base.archivesName)
            dokkaSourceSets.configureEach {
                reportUndocumented.set(false)
            }
        }

    private fun registerJavadocJarTask(vararg dependencies: TaskProvider<*>): TaskProvider<Jar> =
        project.tasks.register<Jar>("javadocJar") {
            archiveClassifier.set("javadoc")
            from(*dependencies)
        }

    private fun registerSourcesJarTask(): TaskProvider<Jar> {
        fun Jar.sourcesFrom(sourceSet: SourceSet) {
            from(sourceSet.java.sourceDirectories)
            from(sourceSet.kotlin.sourceDirectories)
            from(sourceSet.resources.sourceDirectories)
        }

        return project.tasks.register<Jar>("sourcesJar") {
            archiveClassifier.set("sources")
            sourcesFrom(project.java.sourceSets["main"])
            if ("testFixtures" in project.java.sourceSets.names) {
                sourcesFrom(project.java.sourceSets["testFixtures"])
            }
        }
    }
}
