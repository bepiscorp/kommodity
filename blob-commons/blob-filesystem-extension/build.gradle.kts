@Suppress("DSL_SCOPE_VIOLATION") // TODO: Delete after https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    alias(libs.plugins.callisto.kotlinLibrary)
}

dependencies {
    compileOnly(projects.blobCommons.blobCommonsCore)
    compileOnly(projects.dataCommons)
    compileOnly(libs.spring.boot.starter)
}
