plugins {
    kotlin
    id("java-gradle-plugin")
    id("tyrell.callisto.internal.publishing")
}

base.archivesName.set("java-library")
description = "Java Library Convention Plugin: Gradle Plugin to apply Java Library convention."

gradlePlugin {
    plugins.create("javaLibrary") {
        id = "tyrell.callisto.java-library"
        implementationClass = "tyrell.callisto.gradle.plugin.CallistoJavaLibraryPlugin"
    }
}

dependencies {
    api(projects.plugins.base)
    api(projects.plugins.java)

    implementation(gradleApiWithoutKotlin())

    testImplementation(testFixtures(projects.plugins.base))
}
