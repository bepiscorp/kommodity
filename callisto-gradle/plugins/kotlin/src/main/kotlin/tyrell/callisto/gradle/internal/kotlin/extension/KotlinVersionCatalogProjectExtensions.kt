package tyrell.callisto.gradle.internal.kotlin.extension

import org.gradle.api.Project
import org.gradle.api.artifacts.ExternalModuleDependencyBundle
import org.gradle.api.provider.Provider

/*internal*/ val Project.ktlintVersion: Provider<String>
    get() = versionCatalog.flatMap { cat ->
        provider {
            cat.findVersion("gradle.plugins.ktlint").orElseThrow()
                .singularVersion
        }
    }

/*internal*/ val Project.kotestVersion: Provider<String>
    get() = versionCatalog.flatMap { cat ->
        provider {
            cat.findVersion("kotest").orElseThrow()
                .singularVersion
        }
    }

/*internal*/ val Project.kotlinTestingBundle: Provider<ExternalModuleDependencyBundle>
    get() = versionCatalog.flatMap { cat -> cat.findBundle("kotlin-testing").orElseThrow() }

/*internal*/ val Project.kotestBundle: Provider<ExternalModuleDependencyBundle>
    get() = versionCatalog.flatMap { cat -> cat.findBundle("kotest").orElseThrow() }
