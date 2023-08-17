package tyrell.callisto.gradle.plugin

import tyrell.callisto.gradle.internal.AbstractCallistoJavaPlugin

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
class CallistoJavaPlugin : AbstractCallistoJavaPlugin() {

    override fun applyImplementation() {
        project.plugins.apply("java")
    }
}
