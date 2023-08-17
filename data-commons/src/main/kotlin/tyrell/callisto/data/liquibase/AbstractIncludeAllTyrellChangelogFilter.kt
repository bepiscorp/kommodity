package tyrell.callisto.data.liquibase

import liquibase.changelog.IncludeAllFilter
import java.nio.file.Path
import kotlin.io.path.name

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
public abstract class AbstractIncludeAllTyrellChangelogFilter : IncludeAllFilter {

    override fun include(changeLogPath: String): Boolean = Path.of(changeLogPath).let(::matches)

    private fun matches(changelogPath: Path): Boolean = when {
        !isInTyrellChangelogsRoot(changelogPath) -> false
        !isVersionLike(changelogPath.parent.name) -> false
        !accept(changelogPath) -> false
        else -> true
    }

    public abstract fun accept(changelogPath: Path): Boolean

    private companion object {

        private val VERSION_PATTERN: Regex = Regex("^\\d+(\\.\\d+){1,2}$")

        private fun isInTyrellChangelogsRoot(changelogPath: Path): Boolean =
            changelogPath.subpath(0, 2).equals(Path.of("liquibase", "tyrell"))

        private fun isVersionLike(token: String): Boolean = token.matches(VERSION_PATTERN)
    }
}
