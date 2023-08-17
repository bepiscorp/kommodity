plugins {
    `version-catalog`
    `maven-publish`
}

description = "Version Catalog: Callisto version catalog."

catalog {
    versionCatalog {
        from(files("../../gradle/libs.versions.toml"))
    }
}

configurePublishingRepositories()
publishing {
    publications {
        register<MavenPublication>("versionCatalog") {
            groupId = project.group as String
            artifactId = project.name
            version = project.version as String

            from(components["versionCatalog"])
        }
    }
}
