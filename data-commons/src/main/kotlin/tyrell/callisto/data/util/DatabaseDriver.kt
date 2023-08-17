package tyrell.callisto.data.util

import org.springframework.util.StringUtils
import tyrell.callisto.base.definition.LibraryApi
import java.util.Locale

/**
 * Note: Taken from Spring Boot to avoid extra dependency
 *
 * Enumeration of common database drivers.
 *
 * @author Phillip Webb
 * @author Maciej Walkowiak
 * @author Marten Deinum
 * @author Stephane Nicoll
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public enum class DatabaseDriver constructor(

    private val productName: String?,

    /**
     * Return the driver class name.
     * @return the class name or `null`
     */
    public val driverClassName: String?,

    /**
     * Return the XA driver source class name.
     * @return the class name or `null`
     */
    public val xaDataSourceClassName: String? = null,

    /**
     * Return the validation query.
     * @return the validation query or `null`
     */
    public val validationQuery: String? = null,
) {

    /**
     * Unknown type.
     */
    UNKNOWN(null, null),

    /**
     * Apache Derby.
     */
    DERBY(
        "Apache Derby", "org.apache.derby.jdbc.EmbeddedDriver",
        "org.apache.derby.jdbc.EmbeddedXADataSource",
        "SELECT 1 FROM SYSIBM.SYSDUMMY1",
    ),

    /**
     * H2.
     */
    H2("H2", "org.h2.Driver", "org.h2.jdbcx.JdbcDataSource", "SELECT 1"),

    /**
     * HyperSQL DataBase.
     */
    HSQLDB(
        "HSQL Database Engine", "org.hsqldb.jdbc.JDBCDriver",
        "org.hsqldb.jdbc.pool.JDBCXADataSource",
        "SELECT COUNT(*) FROM INFORMATION_SCHEMA.SYSTEM_USERS",
    ),

    /**
     * SQL Lite.
     */
    SQLITE("SQLite", "org.sqlite.JDBC"),

    /**
     * MySQL.
     */
    MYSQL(
        "MySQL", "com.mysql.jdbc.Driver",
        "com.mysql.jdbc.jdbc2.optional.MysqlXADataSource", "SELECT 1",
    ),

    /**
     * Maria DB.
     */
    MARIADB("MySQL", "org.mariadb.jdbc.Driver", "org.mariadb.jdbc.MariaDbDataSource", "SELECT 1") {

        override val id: String = "mysql"
    },

    /**
     * Google App Engine.
     */
    GAE(null, "com.google.appengine.api.rdbms.AppEngineDriver"),

    /**
     * Oracle.
     */
    ORACLE(
        "Oracle", "oracle.jdbc.OracleDriver",
        "oracle.jdbc.xa.client.OracleXADataSource", "SELECT 'Hello' from DUAL",
    ),

    /**
     * Postgres.
     */
    POSTGRESQL(
        "PostgreSQL", "org.postgresql.Driver", "org.postgresql.xa.PGXADataSource",
        "SELECT 1",
    ),

    /**
     * jTDS. As it can be used for several databases, there isn't a single product name we
     * could rely on.
     */
    JTDS(null, "net.sourceforge.jtds.jdbc.Driver"),

    /**
     * SQL Server.
     */
    SQLSERVER(
        "Microsoft SQL Server", "com.microsoft.sqlserver.jdbc.SQLServerDriver",
        "com.microsoft.sqlserver.jdbc.SQLServerXADataSource", "SELECT 1",
    ) {

        override fun matchProductName(productName: String?): Boolean =
            super.matchProductName(productName) ||
                "SQL SERVER".equals(productName, ignoreCase = true)
    },

    /**
     * Firebird.
     */
    FIREBIRD(
        "Firebird", "org.firebirdsql.jdbc.FBDriver",
        "org.firebirdsql.ds.FBXADataSource",
        "SELECT 1 FROM RDB\$DATABASE",
    ) {

        override val urlPrefixes: Collection<String> = setOf("firebirdsql")

        override fun matchProductName(productName: String?): Boolean =
            super.matchProductName(productName) ||
                productName.let { (it != null) && it.lowercase(Locale.getDefault()).startsWith("firebird") }
    },

    /**
     * DB2 Server.
     */
    DB2(
        "DB2", "com.ibm.db2.jcc.DB2Driver", "com.ibm.db2.jcc.DB2XADataSource",
        "SELECT 1 FROM SYSIBM.SYSDUMMY1",
    ) {

        override fun matchProductName(productName: String?): Boolean =
            super.matchProductName(productName) ||
                productName.let { (it != null) && it.lowercase(Locale.getDefault()).startsWith("db2/") }
    },

    /**
     * DB2 AS400 Server.
     */
    DB2_AS400(
        "DB2 UDB for AS/400", "com.ibm.as400.access.AS400JDBCDriver",
        "com.ibm.as400.access.AS400JDBCXADataSource",
        "SELECT 1 FROM SYSIBM.SYSDUMMY1",
    ) {

        override val id: String = "db2"

        override val urlPrefixes: Collection<String> = setOf("as400")

        override fun matchProductName(productName: String?): Boolean =
            super.matchProductName(productName) ||
                productName.let { (it != null) && it.lowercase(Locale.getDefault()).startsWith("db2/") }
    },

    /**
     * Teradata.
     */
    TERADATA("Teradata", "com.teradata.jdbc.TeraDriver"),

    /**
     * Informix.
     */
    INFORMIX(
        "Informix Dynamic Server", "com.informix.jdbc.IfxDriver", null,
        "select count(*) from systables",
    ) {

        override val urlPrefixes: Collection<String> = setOf("informix-sqli", "informix-direct")
    },

    /**
     * PrestoSQL.
     */
    PRESTOSQL("PrestoSQL", "io.prestosql.jdbc.PrestoDriver", null, "SELECT 1") {

        override val urlPrefixes: Collection<String> = setOf("presto")
    },

    /**
     * Trino.
     */
    TRINO("Trino", "io.prestosql.jdbc.PrestoDriver", null, "SELECT 1"),

    /**
     * Apache Phoenix.
     */
    PHOENIX("Apache Phoenix", "org.apache.phoenix.jdbc.PhoenixDriver", null, "SELECT 1 FROM SYSTEM.CATALOG LIMIT 1"),

    /**
     * Testcontainers.
     */
    TESTCONTAINERS(null, "org.testcontainers.jdbc.ContainerDatabaseDriver") {

        override val urlPrefixes: Collection<String> = setOf("tc")
    };

    /**
     * Return the identifier of this driver.
     * @return the identifier
     */
    public open val id: String = name.lowercase(Locale.ENGLISH)

    protected open val urlPrefixes: Collection<String> = setOf(name.lowercase(Locale.ENGLISH))

    protected open fun matchProductName(productName: String?): Boolean {
        return (productName != null) && this.productName.equals(productName, ignoreCase = true)
    }

    public companion object {

        /**
         * Find a [DatabaseDriver] for the given URL.
         *
         * @param url JDBC URL
         * @return the database driver or [UNKNOWN] if not found
         */
        public fun fromJdbcUrl(url: String?): DatabaseDriver {
            if (url.isNullOrEmpty()) return UNKNOWN
            require(url.startsWith("jdbc")) { "URL must start with 'jdbc'" }
            val urlWithoutPrefix = url.substring("jdbc".length).lowercase(Locale.ENGLISH)
            for (driver in values()) {
                for (urlPrefix in driver.urlPrefixes) {
                    val prefix = ":$urlPrefix:"
                    if (driver !== UNKNOWN && urlWithoutPrefix.startsWith(prefix)) {
                        return driver
                    }
                }
            }
            return UNKNOWN
        }

        /**
         * Find a [DatabaseDriver] for the given product name.
         *
         * @param productName product name
         * @return the database driver or [UNKNOWN] if not found
         */
        public fun fromProductName(productName: String?): DatabaseDriver {
            if (StringUtils.hasLength(productName)) {
                for (candidate: DatabaseDriver in values()) {
                    if (candidate.matchProductName(productName)) {
                        return candidate
                    }
                }
            }
            return UNKNOWN
        }
    }
}
