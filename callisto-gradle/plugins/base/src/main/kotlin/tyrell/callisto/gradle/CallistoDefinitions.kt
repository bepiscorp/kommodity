package tyrell.callisto.gradle

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
/*internal*/ object CallistoDefinitions {

    private const val definitionPackage: String = "tyrell.callisto.base.definition"

    val apiAnnotations: List<String> = listOf(
        "InternalLibraryApi", "ObsoleteLibraryApi",
        "DelicateLibraryApi", "ExperimentalLibraryApi",
        "LibraryApi"
    )

    val apiAnnotationsQualifiers: List<String> = apiAnnotations.map { "$definitionPackage.$it" }

    val optInApiAnnotations: List<String> = listOf(
        "InternalLibraryApi", "ObsoleteLibraryApi",
        "DelicateLibraryApi", "ExperimentalLibraryApi"
    )

    val optInApiAnnotationsQualifiers: List<String> = optInApiAnnotations.map { "$definitionPackage.$it" }
}
