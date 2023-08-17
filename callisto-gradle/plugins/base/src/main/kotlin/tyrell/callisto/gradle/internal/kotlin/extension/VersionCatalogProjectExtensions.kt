package tyrell.callisto.gradle.internal.kotlin.extension

import net.swiftzer.semver.SemVer
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.artifacts.VersionConstraint
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.getByType

/*internal*/ val Project.versionCatalog: Provider<VersionCatalog>
    get() = provider { rootProject.extensions.getByType<VersionCatalogsExtension>().single() }

/*internal*/ val VersionConstraint.singularVersion: String
    get() = runCatching { SemVer.parse(requiredVersion) }
        .map(SemVer::toString)
        .getOrElse { preferredVersion }

/*internal*/ val Project.javaVersion: Provider<JavaVersion>
    get() = versionCatalog.flatMap { cat ->
        provider {
            cat.findVersion("java").orElseThrow()
                .requiredVersion.let(JavaVersion::toVersion)
        }
    }

/*internal*/ val Project.kotlinVersion: Provider<KotlinVersion>
    get() = versionCatalog.flatMap { cat ->
        provider {
            cat.findVersion("kotlin")
                .orElseThrow().requiredVersion
                .split('.').map(String::toInt)
                .let { (major, minor, patch) -> KotlinVersion(major, minor, patch) }
        }
    }

/*internal*/ val Project.junitJupiterVersion: Provider<String>
    get() = versionCatalog.flatMap { cat ->
        provider { cat.findVersion("junit.jupiter").orElseThrow().preferredVersion }
    }
