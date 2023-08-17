@file:Suppress("NOTHING_TO_INLINE", "TooManyFunctions")

package tyrell.callisto.data.kotlin.extension

import tyrell.callisto.base.definition.LibraryApi
import java.io.InputStream
import java.math.BigDecimal
import java.sql.ResultSet

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public inline fun ResultSet.getStringOrNull(columnLabel: String): String? =
    takeIfNonNull { getString(columnLabel) }

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public inline fun ResultSet.getBooleanOrNull(columnLabel: String): Boolean? =
    takeIfNonNull { getBoolean(columnLabel) }

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public inline fun ResultSet.getByteOrNull(columnLabel: String): Byte? =
    takeIfNonNull { getByte(columnLabel) }

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public inline fun ResultSet.getShortOrNull(columnLabel: String): Short? =
    takeIfNonNull { getShort(columnLabel) }

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public inline fun ResultSet.getIntOrNull(columnLabel: String): Int? =
    takeIfNonNull { getInt(columnLabel) }

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public inline fun ResultSet.getLongOrNull(columnLabel: String): Long? =
    takeIfNonNull { getLong(columnLabel) }

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public inline fun ResultSet.getFloatOrNull(columnLabel: String): Float? =
    takeIfNonNull { getFloat(columnLabel) }

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public inline fun ResultSet.getDoubleOrNull(columnLabel: String): Double? =
    takeIfNonNull { getDouble(columnLabel) }

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public inline fun ResultSet.getBigDecimalOrNull(columnLabel: String): BigDecimal? =
    takeIfNonNull { getBigDecimal(columnLabel) }

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public inline fun ResultSet.getBytesOrNull(columnLabel: String): ByteArray? =
    takeIfNonNull { getBytes(columnLabel) }

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public inline fun ResultSet.getDateOrNull(columnLabel: String): JavaSqlDate? =
    takeIfNonNull { getDate(columnLabel) }

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public inline fun ResultSet.getTimeOrNull(columnLabel: String): JavaSqlTime? =
    takeIfNonNull { getTime(columnLabel) }

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public inline fun ResultSet.getTimestampOrNull(columnLabel: String): JavaSqlTimestamp? =
    takeIfNonNull { getTimestamp(columnLabel) }

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public inline fun ResultSet.getAsciiStreamOrNull(columnLabel: String): InputStream? =
    takeIfNonNull { getAsciiStream(columnLabel) }

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public inline fun ResultSet.getBinaryStreamOrNull(columnLabel: String): InputStream? =
    takeIfNonNull { getBinaryStream(columnLabel) }

@PublishedApi internal inline fun <T : Any> ResultSet.takeIfNonNull(
    crossinline block: ResultSet.() -> T?,
): T? = block().takeIf { !wasNull() }

private typealias JavaSqlDate = java.sql.Date
private typealias JavaSqlTime = java.sql.Time
private typealias JavaSqlTimestamp = java.sql.Timestamp
