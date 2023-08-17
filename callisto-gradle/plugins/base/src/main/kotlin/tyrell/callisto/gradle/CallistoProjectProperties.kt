package tyrell.callisto.gradle

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
object CallistoProjectProperties {

    private const val PREFIX: String = "callisto"

    /**
     * @see org.gradle.api.Project.getVersion
     * @see tyrell.callisto.gradle.kotlin.dsl.projectVersion
     * @see net.swiftzer.semver.SemVer
     */
    const val PROJECT_VERSION: String = "$PREFIX.projectVersion"

    /**
     * @see org.gradle.api.Project.getVersion
     * @see tyrell.callisto.gradle.kotlin.dsl.buildType
     */
    const val BUILD_TYPE: String = "$PREFIX.buildType"

    /**
     * @see org.gradle.api.Project.getBuildDir
     * @see tyrell.callisto.gradle.kotlin.dsl.globalBuildDirectory
     */
    const val GLOBAL_BUILD_DIR: String = "$PREFIX.globalBuildDir"

    /**
     * @see org.gradle.api.distribution.plugins.DistributionPlugin
     * @see tyrell.callisto.gradle.kotlin.dsl.distributionDirectory
     */
    const val DISTRIBUTION_DIR: String = "$PREFIX.distributionDir"

    /**
     * @see org.gradle.api.plugins.quality.CodeQualityExtension.reportsDir
     * @see org.jetbrains.qodana.QodanaPluginExtension.reportPath
     * @see tyrell.callisto.gradle.kotlin.dsl.reportsDirectory
     */
    const val REPORTS_DIR: String = "$PREFIX.reportsDir"

    /**
     * @see org.jetbrains.dokka.gradle.DokkaMultiModuleTask.outputDirectory
     * @see tyrell.callisto.gradle.kotlin.dsl.documentationDirectory
     */
    const val DOCS_DIR: String = "$PREFIX.documentationDir"

    /**
     * Property responsible for enabling/disabling publishing configuration for project.
     * `true` value enables configuration of publishing.
     * Empty/blank string and `null` values are interpreted as `true`.
     *
     * Type: [Boolean]
     *
     * Default: `false`
     *
     * @see org.gradle.api.publish.plugins.PublishingPlugin
     * @see org.gradle.api.publish.PublishingExtension
     */
    const val PUBLISHING: String = "$PREFIX.publishing"
}
