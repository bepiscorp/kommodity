package tyrell.callisto.base.kotlin.extension

import tyrell.callisto.base.definition.ExperimentalLibraryApi
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.UUID
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

private const val UUID_MAX_TIMESTAMP: Long = 0x0FFF_FFFF_FFFF_FFFF

public const val UUID_MAX_NODE: Long = 0xFFFF_FFFF_FFFF

public const val UUID_MAX_CLOCK_SEQUENCE: Short = 0x3FFF.toShort()

/**
 * Count of periods of 100-nanoseconds length within one second.
 */
private const val TIMESTAMPS_PER_SECOND: Long = 10_000_000L

/**
 * Max amount of seconds since epoch that can be represented as [UUID.timestamp]
 */
public val UUID_MAX_INSTANT: Instant = run {
    // Expected length answer: 36
    val allowedEpochSecondsLength = 60 // timestamp field length within [UUID]
        .minus(TIMESTAMPS_PER_SECOND.takeHighestOneBit().countTrailingZeroBits())
        .minus(1) // Supplemental subtract to fail a lot earlier the moment when bound exceeded
    (1L shl allowedEpochSecondsLength) - 1
}.let(Instant::ofEpochSecond)

/**
 * Count of nanoseconds in one second
 */
private const val NANOS_PER_SECOND: Long = 1_000_000_000L

// UUID version == 1 (Time-based UUID)
private const val UUID_TIME_BASED_VERSION: Long = 0x1

// UUID variant == 2 IETF RFC 4122 (Leach-Salz)
private const val UUID_RFC4122_VARIANT: Long = 0x2

private const val DEFAULT_TIMESTAMP: Long = 0

private const val DEFAULT_NODE: Long = 0

private const val DEFAULT_CLOCK_SEQUENCE: Short = 0

@SuppressWarnings("MagicNumber")
private val TIME_SINCE_REFORM_UNTIL_EPOCH: Duration = LocalDateTime.of(
    1582, 10, 15,
    0, 0, 0, 0,
).toInstant(ZoneOffset.UTC)
    .toEpochMilli()
    .toDuration(DurationUnit.MILLISECONDS)
    .absoluteValue

public val UUID.version: Int get() = version()

public val UUID.variant: Int get() = variant()

public val UUID.timestamp: Long get() = timestamp()

public val UUID.clockSequence: Short get() = clockSequence().toShort()

public val UUID.node: Long get() = node()

public val UUID.instant: Instant
    get() = Instant.ofEpochSecond(
        (timestamp / TIMESTAMPS_PER_SECOND) - TIME_SINCE_REFORM_UNTIL_EPOCH.inWholeSeconds,
        (timestamp % TIMESTAMPS_PER_SECOND) * (NANOS_PER_SECOND / TIMESTAMPS_PER_SECOND),
    )

/**
 * Converts [String] to instance of [UUID].
 *
 * Shortcut function for:
 * ```
 * UUID.fromString(value)
 * ```
 *
 * @return instance of [UUID]
 * @author Mikhail Gostev
 * @since 0.1.0
 */
@Suppress("NOTHING_TO_INLINE")
public inline fun String.toUuid(): UUID = UUID.fromString(this)

/**
 * Converts given integer to [UUID]
 * Given value is used as last 48 least significant bits of UUID.
 *
 * @author Mikhail Gostev
 * @since 0.0.1
 * @return UUID created from given value
 */
@ExperimentalLibraryApi
public fun timeBasedUuid(
    instant: Instant = Instant.now(),
    node: Long = DEFAULT_NODE,
    clockSequence: Short = DEFAULT_CLOCK_SEQUENCE,
): UUID {
    require(instant <= UUID_MAX_INSTANT) { "Exceeded maximum instant value [$UUID_MAX_INSTANT]" }
    require(instant.nano.mod(NANOS_PER_SECOND / TIMESTAMPS_PER_SECOND) == 0L) {
        "The precision of nanos in given instant value [$instant] is not supported"
    }

    require(node >= 0) { "Only non-negative value is allowed for [node]" }
    require(node <= UUID_MAX_NODE) { "Maximal value of [node] ($UUID_MAX_NODE) has been exceeded" }

    require(clockSequence >= 0) { "Only non-negative value is allowed for [clockSequence]" }
    require(clockSequence <= UUID_MAX_CLOCK_SEQUENCE) {
        "Maximal value of [clockSequence] ($UUID_MAX_CLOCK_SEQUENCE) has been exceeded"
    }

    return timeBasedUuid(
        timestamp = instant.toUuidTimestamp(),
        node = node,
        clockSequence = clockSequence,
    )
}

/**
 *
 * The timestamp is a 60-bit value. For UUID version 1, this is
 * represented by Coordinated Universal Time (UTC) as a count of
 * 100-nanosecond intervals since 00:00:00.00, 15 October 1582
 * (the date of Gregorian reform to the Christian calendar).
 *
 * @return amount of 100-nanos intervals since midnight 15.10.1582 until given time
 * @see TIME_SINCE_REFORM_UNTIL_EPOCH
 */
private fun Instant.toUuidTimestamp(): Long =
    epochSecond.toDuration(DurationUnit.SECONDS)
        .plus(TIME_SINCE_REFORM_UNTIL_EPOCH).inWholeSeconds
        .times(TIMESTAMPS_PER_SECOND)
        .plus(nano.div(NANOS_PER_SECOND / TIMESTAMPS_PER_SECOND))

/**
 * See [RFC 4122 A Universally Unique IDentifier (UUID) URN Namespace](https://www.ietf.org/rfc/rfc4122.txt)
 * ```
 * 0                   1                   2                   3
 *  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |                          time_low                             |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |       time_mid                |         time_hi_and_version   |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |clk_seq_hi_res |  clk_seq_low  |         node (0-1)            |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |                         node (2-5)                            |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * ```
 *
 * @since 0.0.1
 * @author Mikhail Gostev
 */
@SuppressWarnings("MagicNumber")
private fun timeBasedUuid(
    timestamp: Long = DEFAULT_TIMESTAMP,
    node: Long = DEFAULT_NODE,
    clockSequence: Short = DEFAULT_CLOCK_SEQUENCE,
): UUID {
    require(timestamp >= 0) { "Only non-negative value is allowed for [timestamp]" }
    require(timestamp <= UUID_MAX_TIMESTAMP) { "Maximal value of [timestamp] ($UUID_MAX_TIMESTAMP) has been exceeded" }

    require(node >= 0) { "Only non-negative value is allowed for [node]" }
    require(node <= UUID_MAX_NODE) { "Maximal value of [node] ($UUID_MAX_NODE) has been exceeded" }

    require(clockSequence >= 0) { "Only non-negative value is allowed for [clockSequence]" }
    require(clockSequence <= UUID_MAX_CLOCK_SEQUENCE) {
        "Maximal value of [clockSequence] ($UUID_MAX_CLOCK_SEQUENCE) has been exceeded"
    }

    /**
     * Timestamp layout (`timestamp`):
     * ```
     * 0x0FFF000000000000 time_hi  (offset: 48, length: 12)
     * 0x0000FFFF00000000 time_mid (offset: 32, length: 16)
     * 0x00000000FFFFFFFF time_low (offset: 00, length: 32)
     * ```
     */
    val timeHi: Long = (timestamp shr 48) and 0x0FFF
    val timeMid: Long = (timestamp shr 32) and 0xFFFF
    val timeLow: Long = timestamp and 0xFFFF_FFFF

    /**
     * Most significant bits:
     * ```
     * 0xFFFFFFFF00000000 time_low (offset: 32, length: 32)
     * 0x00000000FFFF0000 time_mid (offset: 16, length: 16)
     * 0x000000000000F000 version  (offset: 12, length: 04)
     * 0x0000000000000FFF time_hi  (offset: 00, length: 12)
     * ```
     */
    val mostSigBits: Long = (timeLow shl 32) or (timeMid shl 16) or (UUID_TIME_BASED_VERSION shl 12) or timeHi

    /**
     * Least significant bits:
     * ```
     * 0xC000000000000000 variant   (offset: 62, length: 03)
     * 0x3FFF000000000000 clock_seq (offset: 48, length: 14)
     * 0x0000FFFFFFFFFFFF node      (offset: 00, length: 48)
     * ```
     */
    val leastSigBits: Long = (UUID_RFC4122_VARIANT shl 62) or (clockSequence.toLong() shl 48) or node

    return UUID(mostSigBits, leastSigBits)
}
