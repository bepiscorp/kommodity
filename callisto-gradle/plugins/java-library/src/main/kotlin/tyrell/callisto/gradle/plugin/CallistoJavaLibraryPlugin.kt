package tyrell.callisto.gradle.plugin

import tyrell.callisto.gradle.internal.AbstractCallistoJavaPlugin

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@Suppress("unused")
class CallistoJavaLibraryPlugin : AbstractCallistoJavaPlugin() {

    override fun applyImplementation() {
        project.plugins.apply("java-library")
    }
}
