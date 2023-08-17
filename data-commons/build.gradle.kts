@Suppress("DSL_SCOPE_VIOLATION") // TODO: Delete after https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    alias(libs.plugins.callisto.kotlinLibrary)
}

dependencies {
    compileOnly(libs.spring.contextSupport)
    compileOnly(libs.liquibase.core)

    api(projects.baseCommons)
    api(libs.spring.data.commons)
    api(libs.spring.tx)
    api(libs.bundles.jackson)
}
