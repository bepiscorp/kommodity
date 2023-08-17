package tyrell.callisto.test.kotlin.extension

import tyrell.callisto.base.definition.LibraryApi
import tyrell.callisto.base.kotlin.extension.UUID_MAX_CLOCK_SEQUENCE
import tyrell.callisto.base.kotlin.extension.UUID_MAX_INSTANT
import tyrell.callisto.base.kotlin.extension.UUID_MAX_NODE
import tyrell.callisto.base.kotlin.extension.timeBasedUuid
import java.time.Instant
import java.time.LocalDateTime
import java.time.Month
import java.time.ZoneOffset
import java.util.UUID
import kotlin.time.DurationUnit

/**
 * `November 15th, 2019`.
 * Base time relatively which
 * [Tyrell](https://bladerunner.fandom.com/wiki/Eldon_Tyrell) met with _Blade Runner Rick Deckard_,
 * who arrived at the [Tyrell Corporation](https://bladerunner.fandom.com/wiki/Tyrell_Corporation)
 * after being ordered by Captain
 */
private val DEFAULT_INSTANT: Instant = LocalDateTime.of(
    2019, Month.NOVEMBER,
    15, 10, 0,
).toInstant(ZoneOffset.UTC)

private const val DEFAULT_NODE: Long = 0x0000_0000_DEAD_BEEF

private const val DEFAULT_CLOCK_SEQUENCE: Short = 0x0000

/**
 * Converts int to [UUID] setting given value as [UUID.timestamp]
 * Given int MUST NOT exceed [UUID_MAX_INSTANT] - [DEFAULT_INSTANT].
 *
 * Given number is interpreted as [DurationUnit.SECONDS] and resolved
 * relatively to [DEFAULT_INSTANT] instant.
 *
 * The implementation without defining defaultable arguments is equivalent for this path to:
 * ```
 * return timeBasedUuid(instant = DEFAULT_INSTANT.plusSeconds(this.toLong()))
 * ```
 *
 * @author Mikhail Gostev
 * @author Vyacheslav Dobrynin
 * @since 0.0.1
 * @return UUID created from given value
 * @see DEFAULT_INSTANT
 */
@LibraryApi
public fun Int.toUuidTimestamp(
    node: Long = DEFAULT_NODE,
    clockSequence: Short = DEFAULT_CLOCK_SEQUENCE,
): UUID = timeBasedUuid(
    instant = DEFAULT_INSTANT.plusSeconds(this.toLong()),
    node = node,
    clockSequence = clockSequence,
)

/**
 * Converts long to [UUID] setting given value as [UUID.node]
 * Given int MUST NOT exceed [UUID_MAX_NODE].
 *
 * The implementation without defining defaultable arguments is equivalent for this path to:
 * ```
 * return timeBasedUuid(node = this)
 * ```
 *
 * @author Mikhail Gostev
 * @since 0.0.1
 * @return UUID created from given value
 */
@LibraryApi
public fun Long.toUuidNode(
    instant: Instant = DEFAULT_INSTANT,
    clockSequence: Short = DEFAULT_CLOCK_SEQUENCE,
): UUID = timeBasedUuid(
    node = this,
    instant = instant,
    clockSequence = clockSequence,
)

/**
 * Converts short to [UUID] setting given value as [UUID.clockSequence]
 * Given short MUST NOT exceed [UUID_MAX_CLOCK_SEQUENCE]
 *
 * The implementation without defining defaultable arguments is equivalent for this path to:
 * ```
 * return timeBasedUuid(clockSequence = this)
 * ```
 *
 * @author Mikhail Gostev
 * @since 0.0.1
 * @return UUID created from given value
 */
@LibraryApi
public fun Short.toUuidClockSeq(
    instant: Instant = DEFAULT_INSTANT,
    node: Long = DEFAULT_NODE,
): UUID = timeBasedUuid(
    clockSequence = this,
    instant = instant,
    node = node,
)
