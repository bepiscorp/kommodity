@Suppress("DSL_SCOPE_VIOLATION") // TODO: Delete after https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    alias(libs.plugins.callisto.kotlinLibrary)
}

apply<JavaTestFixturesPlugin>()

dependencies {
    compileOnly(projects.dataCommons)
    compileOnly(libs.bundles.jackson)

    api(libs.bundles.kotest)
    api(libs.bundles.kotlin.testing)
    api(libs.bundles.kotlin.base)

    testImplementation(projects.baseCommons)
}
