package tyrell.callisto.gradle.kotlin.dsl

import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import java.net.URI

typealias MavenArtifactRepositoryMixin = MavenArtifactRepository.() -> Unit

/**
 * Cybernation repository readonly group that aggregates:
 * - `cybernation-releases`, `cybernation-snapshots`
 * - `maven-central`, `maven-central-snapshots`
 * - `maven-clojars`, `maven-clojars-snapshots`
 * - `maven-private`, `maven-manual`
 *
 * @author Mikhail Gostev
 * @since 0.4.0
 */
@JvmOverloads fun RepositoryHandler.cybernationAll(
    mixinBlock: MavenArtifactRepositoryMixin = {}
): MavenArtifactRepository = maven { repo: MavenArtifactRepository ->
    repo.name = "cybernationAll"

    commonMixin(repo)
    mixinBlock(repo)

    repo.url = URI.create("https://nexus.cybernation.com/repository/cybernation-all")
    repo.mavenContent { it.snapshotsOnly() }
}

/**
 * Cybernation proxy for Maven Central Repository
 *
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@JvmOverloads fun RepositoryHandler.mavenCentralDelegate(
    mixinBlock: MavenArtifactRepositoryMixin = {}
): MavenArtifactRepository = maven { repo: MavenArtifactRepository ->
    repo.name = "mavenCentralDelegate"

    commonMixin(repo)
    mixinBlock(repo)

    repo.url = URI.create("https://repo1.maven.org/maven2")
    repo.mavenContent { it.releasesOnly() }
}

/**
 * Cybernation proxy for Gradle Plugins Repository
 *
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@JvmOverloads fun RepositoryHandler.gradlePluginPortalDelegate(
    mixinBlock: MavenArtifactRepositoryMixin = {}
): MavenArtifactRepository = maven { repo: MavenArtifactRepository ->
    repo.name = "gradlePluginsPortalDelegate"

    commonMixin(repo)
    mixinBlock(repo)

    repo.url = URI.create("https://plugins.gradle.org/m2")
    repo.mavenContent { it.releasesOnly() }
}

/**
 * Cybernation proxy for Spring Release Repository
 *
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@JvmOverloads fun RepositoryHandler.springReleaseDelegate(
    mixinBlock: MavenArtifactRepositoryMixin = {}
): MavenArtifactRepository = maven { repo: MavenArtifactRepository ->
    repo.name = "springReleaseDelegate"

    commonMixin(repo)
    mixinBlock(repo)

    repo.url = URI.create("https://repo.spring.io/release") // TODO: Replace with Cybernation proxy
    repo.mavenContent { it.releasesOnly() }
}

/**
 * Cybernation proxy for Spring Plugins Repository
 *
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@JvmOverloads fun RepositoryHandler.springPluginsDelegate(
    mixinBlock: MavenArtifactRepositoryMixin = {}
): MavenArtifactRepository = maven { repo: MavenArtifactRepository ->
    repo.name = "springPluginsDelegate"

    commonMixin(repo)
    mixinBlock(repo)

    repo.url = URI.create("https://repo.spring.io/plugins-release")
    repo.mavenContent { it.releasesOnly() }
}

/**
 * Cybernation repository for snapshots
 *
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@JvmOverloads fun RepositoryHandler.snapshotsRepo(
    mixinBlock: MavenArtifactRepositoryMixin = {}
): MavenArtifactRepository = maven { repo: MavenArtifactRepository ->
    repo.name = "snapshotsRepo"

    commonMixin(repo)
    mixinBlock(repo)

    // TODO: Replace with corporate SNAPSHOT repo
    repo.url = URI.create("https://repo1.maven.org/maven2")
    repo.mavenContent { it.snapshotsOnly() }
}

/**
 * Cybernation repository for releases
 *
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@JvmOverloads fun RepositoryHandler.releasesRepo(
    mixinBlock: MavenArtifactRepositoryMixin = {}
): MavenArtifactRepository = maven { repo: MavenArtifactRepository ->
    repo.name = "releasesRepo"

    commonMixin(repo)
    mixinBlock(repo)

    // TODO: Replace with corporate release repo
    repo.url = URI.create("https://repo1.maven.org/maven2")
    repo.mavenContent { it.releasesOnly() }
}

private val commonMixin: MavenArtifactRepositoryMixin = {
    isAllowInsecureProtocol = false
}
