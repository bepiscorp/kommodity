plugins {
    kotlin
    `java-library`
    `java-test-fixtures`
    id("java-gradle-plugin")
    id("tyrell.callisto.internal.publishing")
}

// val functionalTest: SourceSet by sourceSets.creating
// val integrationTest: SourceSet by sourceSets.creating

base.archivesName.set("base")
description = "Base Plugin: Plugin and utility functions/classes to develop plugins."

gradlePlugin {
    plugins.create("tyrell.callisto.base") {
        id = "tyrell.callisto.base"
        implementationClass = "tyrell.callisto.gradle.plugin.CallistoBasePlugin"
    }
}

// configurations["functionalTestImplementation"].extendsFrom(configurations["testImplementation"])
// configurations["integrationTestImplementation"].extendsFrom(configurations["testImplementation"])

// val functionalTestTask: Test by tasks.register<Test>("functionalTest") {
//     description = "Runs the functional tests."
//     group = "verification"
//     mustRunAfter(tasks.test)
//
//     testClassesDirs = functionalTest.output.classesDirs
//     classpath = functionalTest.runtimeClasspath
// }

// val integrationTestTask: Test by tasks.register<Test>("integrationTest") {
//     description = "Runs the integration tests."
//     group = "verification"
//     mustRunAfter(tasks.test)
//
//     testClassesDirs = integrationTest.output.classesDirs
//     classpath = integrationTest.runtimeClasspath
// }

// Run the functional and integration tests as part of `check`
tasks.check {
    // dependsOn(integrationTestTask)
    // dependsOn(functionalTestTask)
}

dependencies {
    api(projects.tyrellConventions)

    implementation(gradleApiWithoutKotlin())

    api(libs.semver)
    implementation(libs.slf4j.api)
    implementation(libs.kotlinLogging.jvm)

    implementation(libs.gradle.plugins.kotlin)
    implementation(libs.gradle.plugins.dokka)
    implementation(libs.gradle.plugins.spotless)

    implementation(libs.bundles.kotlinx.coroutines)
    implementation(libs.bundles.kotlin.base)

    // "integrationTestImplementation"(project)
    // testImplementation(libs.bundles.kotlin.testing)
}
