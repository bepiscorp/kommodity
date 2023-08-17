package tyrell.callisto.gradle.internal

import java.net.URI
import org.gradle.api.Project
import org.gradle.api.artifacts.repositories.PasswordCredentials
import org.gradle.api.publish.PublishingExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.credentials
import org.jetbrains.kotlin.gradle.targets.js.npm.SemVer // TODO: should be replaced with 'net.swiftzer.semver.SemVer'

internal fun Project.configurePublishingRepositories() = SemVer.from(version as String).run {
    // TODO: Configure publishing to corporate repos
    // === CUT HERE
    project.configure<PublishingExtension> {
        repositories {
            mavenLocal()
        }
    }
    // ... and replace with the code below when corporate Nexus is ready:
    //when {
    //    (preRelease == "SNAPSHOT") -> configurePublishingRepository(
    //        "cybernationSnapshots",
    //        "https://nexus.cybernation.com/repository/cybernation-snapshots"
    //    )
    //    else -> configurePublishingRepository(
    //        "cybernationReleases",
    //        "https://nexus.cybernation.com/repository/cybernation-releases"
    //    )
    //}
}

private fun Project.configurePublishingRepository(repositoryName: String, repositoryUrl: String) {
    project.configure<PublishingExtension> {
        repositories {
            maven {
                name = repositoryName
                url = URI.create(repositoryUrl)
                isAllowInsecureProtocol = false
                credentials(PasswordCredentials::class)
            }
        }
    }
}
