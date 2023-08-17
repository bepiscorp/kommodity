package tyrell.callisto.gradle.internal.repository

import java.nio.charset.StandardCharsets

object JupiterMoonsRepository {

    private const val RESOURCE_FILE_NAME: String = "jupiter_moons.txt"

    fun getJupiterMoons(): List<String> =
        this::class.java.classLoader
            .getResourceAsStream(RESOURCE_FILE_NAME)
            .let(::checkNotNull)
            .reader(StandardCharsets.UTF_8)
            .readLines()
}
