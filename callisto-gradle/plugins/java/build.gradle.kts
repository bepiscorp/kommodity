plugins {
    kotlin
    id("java-gradle-plugin")
    id("tyrell.callisto.internal.publishing")
}

base.archivesName.set("java")
description = "Java Convention Plugin: Gradle Plugin to apply Java convention."

gradlePlugin {
    plugins.create("java") {
        id = "tyrell.callisto.java"
        implementationClass = "tyrell.callisto.gradle.plugin.CallistoJavaPlugin"
    }
}

dependencies {
    api(projects.plugins.base)

    implementation(gradleApiWithoutKotlin())

    testImplementation(testFixtures(projects.plugins.base))
}
