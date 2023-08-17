package tyrell.callisto.data.liquibase

import tyrell.callisto.base.definition.InternalLibraryApi
import java.nio.file.Path
import kotlin.io.path.name

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@InternalLibraryApi
public class IncludeAllDataTyrellChangelogFilter : AbstractIncludeAllTyrellChangelogFilter() {

    override fun accept(changelogPath: Path): Boolean =
        isSchemaChangelog(changelogPath.name) || isSqlSchemaChangelog(changelogPath.name)

    private companion object {

        private val SCHEMA_CHANGELOG_FILENAME_REGEX: Regex = Regex("^(.+-)*data.changelog.xml$")

        private val SCHEMA_SQL_CHANGELOG_FILENAME_REGEX: Regex = Regex("^(.+-)*data.changelog.sql$")

        private fun isSchemaChangelog(changelogFileName: String): Boolean =
            changelogFileName.matches(SCHEMA_CHANGELOG_FILENAME_REGEX)

        private fun isSqlSchemaChangelog(changelogFileName: String): Boolean =
            changelogFileName.matches(SCHEMA_SQL_CHANGELOG_FILENAME_REGEX)
    }
}
