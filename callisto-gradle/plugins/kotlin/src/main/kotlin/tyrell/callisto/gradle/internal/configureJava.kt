package tyrell.callisto.gradle.internal

import org.gradle.api.Project
import org.gradle.api.plugins.JavaBasePlugin
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.jvm.toolchain.JavaToolchainSpec
import org.gradle.jvm.toolchain.JvmVendorSpec
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import tyrell.callisto.gradle.internal.kotlin.extension.javaVersion

@Suppress("UNUSED_PARAMETER")
internal fun Project.configureJava(callisto: CallistoProject.Module) {
    logger.debug("Configuring Java")
    with(project) {
        apply<JavaBasePlugin>()

        extensions.configure<JavaPluginExtension> {
            toolchain { spec: JavaToolchainSpec ->
                spec.vendor.set(JvmVendorSpec.ADOPTIUM)
                spec.languageVersion.set(JavaLanguageVersion.of(javaVersion.get().toString()))
            }
            withJavadocJar()
            withSourcesJar()
        }
    }
}
