plugins {
    kotlin
    id("java-gradle-plugin")
    id("tyrell.callisto.internal.publishing")
}

base.archivesName.set("kotlin")
description = "Kotlin Convention Plugin: Gradle Plugin to apply Kotlin convention."

gradlePlugin {
    plugins.create("kotlin") {
        id = "tyrell.callisto.kotlin"
        implementationClass = "tyrell.callisto.gradle.plugin.CallistoKotlinPlugin"
    }
}

dependencies {
    api(projects.plugins.base)

    api(libs.gradle.plugins.spring.boot)
    api(libs.gradle.plugins.kotlin)
    api(libs.gradle.plugins.kotlin.allopen)
    api(libs.gradle.plugins.kotlin.noarg)
    api(libs.gradle.plugins.kotlinx.kover)
    api(libs.gradle.plugins.dokka)
    api(libs.gradle.plugins.spotless)
    api(libs.gradle.plugins.detekt)
    api(libs.gradle.plugins.shadow)
    implementation(gradleApiWithoutKotlin())

    testImplementation(testFixtures(projects.plugins.base))
}
