import org.jetbrains.kotlin.allopen.gradle.SpringGradleSubplugin

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Delete after https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    alias(libs.plugins.callisto.kotlinLibrary)
}

apply<SpringGradleSubplugin>()

dependencies {
    compileOnly(projects.dataCommons)
    compileOnly(libs.spring.boot.starter)
    compileOnly(libs.spring.contextSupport)
}
