plugins {
    kotlin
    `java-library`
    id("tyrell.callisto.internal.publishing")
}

base.archivesName.set("tyrell-conventions")
description = "Tyrell Conventions: Utility functions and classes with Tyrell conventions."

dependencies {
    implementation(gradleApiWithoutKotlin())
    implementation(libs.caffeine)

    implementation(libs.reactor.kotlin.extensions)
    implementation(libs.kotlinx.coroutines.reactor)
    implementation(libs.bundles.kotlinx.coroutines)
    implementation(libs.bundles.kotlin.base)
}
