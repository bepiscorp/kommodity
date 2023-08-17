import org.jetbrains.kotlin.allopen.gradle.SpringGradleSubplugin
import org.jetbrains.kotlin.noarg.gradle.KotlinJpaSubplugin

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Delete after https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    alias(libs.plugins.callisto.kotlinLibrary)
}

apply<SpringGradleSubplugin>()
apply<KotlinJpaSubplugin>()

dependencies {
    api(projects.dataCommons)
    api(libs.spring.data.jpa)
    api(libs.hibernate.envers)

    testImplementation(testFixtures(projects.testCommons))
}
