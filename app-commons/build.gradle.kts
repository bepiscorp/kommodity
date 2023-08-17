@Suppress("DSL_SCOPE_VIOLATION") // TODO: Delete after https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    alias(libs.plugins.callisto.kotlinLibrary)
}

dependencies {
    runtimeOnly(libs.liquibase.core)

    api(projects.baseCommons)
    api(libs.spring.boot.starter)
    api(libs.spring.boot)

    implementation(libs.spring.contextSupport)
    implementation(libs.bundles.kotlin.base)
    implementation(libs.bundles.log4jRuntime)
}
