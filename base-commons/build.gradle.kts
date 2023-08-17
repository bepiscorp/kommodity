@Suppress("DSL_SCOPE_VIOLATION") // TODO: Delete after https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    alias(libs.plugins.callisto.kotlinLibrary)
}

dependencies {
    compileOnly(libs.spring.contextSupport)
    compileOnly(libs.spring.data.commons)

    compileOnly(libs.jackson.core)
    compileOnly(libs.jackson.databind)
    compileOnly(libs.jackson.annotations)
    compileOnly(libs.jackson.dataformat.xml)

    api(libs.reactor.kotlin.extensions)
    api(libs.kotlinx.coroutines.reactor)
    api(libs.bundles.kotlinx.coroutines)
    api(libs.bundles.kotlin.base)

    api(libs.bundles.log4j)
    api(libs.kotlinLogging.jvm)

    testApi(testFixtures(projects.testCommons))
}
