plugins {
    kotlin
    id("java-gradle-plugin")
    id("tyrell.callisto.internal.publishing")
}

base.archivesName.set("kotlin-library")
description = "Kotlin Convention Plugin: Gradle Plugin to apply Kotlin Library convention."

gradlePlugin {
    plugins.create("kotlin-library") {
        id = "tyrell.callisto.kotlin-library"
        implementationClass = "tyrell.callisto.gradle.plugin.CallistoKotlinLibraryPlugin"
    }
}

dependencies {
    api(projects.plugins.kotlin)

    implementation(gradleApiWithoutKotlin())

    testImplementation(testFixtures(projects.plugins.base))
}
