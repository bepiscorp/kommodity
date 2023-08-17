import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        mavenLocal()
        maven(url = "https://repo1.maven.org/maven2")
    }

    dependencies {
        classpath(kotlin("gradle-plugin"))
    }
}

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

repositories {
    mavenLocal()
    maven(url = "https://repo1.maven.org/maven2")
    maven(url = "https://plugins.gradle.org/m2")
}

dependencies {
    implementation(deps.gradle.plugins.kotlin)
    implementation(enforcedPlatform(deps.kotlin.bom))
    implementation(deps.kotlin.compilerEmbeddable)
    implementation(deps.gradle.plugins.dokka)
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.verbose = true
    kotlinOptions.allWarningsAsErrors = true
    kotlinOptions.freeCompilerArgs += listOf(
        "-Xskip-runtime-version-check"
    )
}

gradlePlugin {
    plugins {
        create("publishing") {
            id = "tyrell.callisto.internal.publishing"
            implementationClass = "tyrell.callisto.gradle.internal.PublishingPlugin"
        }
    }
}
