enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "callisto-gradle"

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenLocal()
        // TODO: Replace with corporate SNAPSHOT repo
        // maven {
        //     url = uri("https://nexus.cybernation.com/repository/cybernation-snapshots")
        //     mavenContent { snapshotsOnly() }
        // }
        // TODO: Replace with corporate release repo
        // maven {
        //     url = uri("https://nexus.cybernation.com/repository/cybernation-releases")
        //     mavenContent { releasesOnly() }
        // }
        maven {
            url = uri("https://repo.gradle.org/gradle/libs-releases-local") // TODO: Replace with Tyrell proxy
            mavenContent { releasesOnly() }
        }
        maven {
            url = uri("https://repo1.maven.org/maven2")
            mavenContent { releasesOnly() }
        }
        maven {
            url = uri("https://plugins.gradle.org/m2")
            mavenContent { releasesOnly() }
        }
    }

    @Suppress("UnstableApiUsage")
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

// Include subprojects
fileTree(rootProject.projectDir) {
    include("**/build.gradle.kts")
    exclude("build.gradle.kts") // Exclude root build.gradle.kts
    exclude("**/buildSrc") // Exclude build sources
    exclude(".*") // Exclude hidden sources
}
    .asSequence()
    .map { relativePath(it.parent).replace(File.separator, ":") }
    .forEach { include(it) }
