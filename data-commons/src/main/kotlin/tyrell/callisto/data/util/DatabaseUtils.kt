package tyrell.callisto.data.util

import org.springframework.util.ClassUtils
import tyrell.callisto.base.definition.ExperimentalLibraryApi
import tyrell.callisto.data.util.DatabaseDriver.H2
import tyrell.callisto.data.util.DatabaseDriver.ORACLE
import tyrell.callisto.data.util.DatabaseDriver.POSTGRESQL
import tyrell.callisto.data.util.DatabaseDriver.PRESTOSQL

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@ExperimentalLibraryApi
public object DatabaseUtils {

    private const val HIBERNATE_PRESENCE_MARKER_CLASS = "org.hibernate.Session"

    private val DATABASE_BY_TYPE: Map<String, DatabaseDriver> = mapOf(
        DatabaseTypes.ORACLE to ORACLE,
        DatabaseTypes.POSTGRESQL to POSTGRESQL,
        DatabaseTypes.H2 to H2,
        DatabaseTypes.PRESTO to PRESTOSQL,
    )

    private val HIBERNATE_DIALECT_CLASS_NAME_BY_DATABASE: Map<DatabaseDriver, String> = mapOf(
        ORACLE to HibernateDialects.ORACLE,
        POSTGRESQL to HibernateDialects.POSTGRES,
        H2 to HibernateDialects.H2,
    )

    private val DEFAULT_DATABASE: DatabaseDriver by lazy {
        val resolvedTypes = DATABASE_BY_TYPE.values.filter {
            ClassUtils.isPresent(it.driverClassName!!, DatabaseUtils::class.java.classLoader)
        }

        when {
            resolvedTypes.isEmpty() -> throw IllegalStateException("Could not find any supported JDBC driver")
            resolvedTypes.size > 1 -> throw IllegalStateException(
                "More than one JDBC drivers exist: $resolvedTypes. Use explicit configuration",
            )
            else -> resolvedTypes[0]
        }
    }

    @JvmStatic
    public fun resolveDatabase(type: String): DatabaseDriver {
        return DATABASE_BY_TYPE[type] ?: getDefaultDatabase()
    }

    @JvmStatic
    public fun getSupportedDatabases(): Map<String, DatabaseDriver> = DATABASE_BY_TYPE

    @JvmStatic
    public fun getDefaultDatabase(): DatabaseDriver = DEFAULT_DATABASE

    /**
     * Resolve Hibernate dialect class by given [DatabaseDriver] parameter
     *
     * @param database parameter to resolve Hibernate dialect from
     * @return full class name of resolved Hibernate dialect
     *
     * @throws IllegalArgumentException if given [database] parameter is not supported
     * @throws IllegalStateException if could not resolve dialect for given [database] parameter
     */
    @JvmStatic
    public fun resolveHibernateDialect(database: DatabaseDriver): String {
        if (database !in DATABASE_BY_TYPE.values) {
            throw IllegalArgumentException("Database $database is not supported")
        }

        return HIBERNATE_DIALECT_CLASS_NAME_BY_DATABASE[database]
            ?: throw IllegalStateException("Could not resolve Hibernate dialect name for $database")
    }

    /**
     * Whether Hibernate present in the classpath
     *
     * @return true if Hibernate present in the classpath, false otherwise
     */
    @JvmStatic
    public fun isHibernateInClassPath(): Boolean {
        return ClassUtils.isPresent(HIBERNATE_PRESENCE_MARKER_CLASS, DatabaseUtils::class.java.classLoader)
    }

    public object DatabaseTypes {

        public const val ORACLE: String = "oracle"

        public const val POSTGRESQL: String = "postgres"

        public const val H2: String = "h2"

        public const val PRESTO: String = "presto"
    }

    public object HibernateDialects {

        public const val ORACLE: String = "org.hibernate.dialect.Oracle10gDialect"

        public const val POSTGRES: String = "org.hibernate.dialect.PostgreSQL94Dialect"

        public const val H2: String = "org.hibernate.dialect.H2Dialect"
    }
}
