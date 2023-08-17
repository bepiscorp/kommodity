package io.kotest.matchers.uuids

import io.kotest.assertions.fail
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import java.util.UUID
import tyrell.callisto.base.definition.ExperimentalLibraryApi
import tyrell.callisto.base.kotlin.dsl.uncheckedCast
import tyrell.callisto.base.kotlin.extension.clockSequence
import tyrell.callisto.base.kotlin.extension.instant
import tyrell.callisto.base.kotlin.extension.node
import tyrell.callisto.base.kotlin.extension.padCenter
import tyrell.callisto.base.kotlin.extension.spaces
import tyrell.callisto.base.kotlin.extension.timestamp
import tyrell.callisto.base.kotlin.extension.variant
import tyrell.callisto.base.kotlin.extension.version

/**
 * Checks whether given [UUID] instance is valid
 * @author Mikhail Gostev
 * @since 0.1.0
 */
@ExperimentalLibraryApi
public fun UUID.shouldBeValid() {
    this shouldBe uuidValidityMatcher(this)
}

/**
 * Checks whether given [UUID] instance equals [expected] value.
 * @author Mikhail Gostev
 * @since 0.1.0
 */
public infix fun UUID?.shouldBe(expected: UUID): UUID {
    if (this == null) fail("Value cannot be null")
    this should UuidEqualsMatcher(expected)
    return this
}

private fun uuidValidityMatcher(uuid: UUID): Matcher<UUID> = compose(
    versionValidityMatcher(uuid.version()) to { it.version },
    variantValidityMatcher(uuid.variant()) to { it.variant },
    timestampValidityMatcher(uuid.timestamp()) to { it.timestamp },
    clockSequenceValidityMatcher(uuid.clockSequence().toShort()) to { it.clockSequence },
    nodeValidityMatcher(uuid.node()) to { it.node },
)

/**
 * @see io.kotest.matchers.reflection.compose
 */
private fun <T : Any?> compose(
    vararg pairs: Pair<Matcher<*>, (T) -> Any>,
): Matcher<T> = object : Matcher<T> {
    override fun test(value: T): MatcherResult {
        val results = pairs.map { (matcher, resolver) ->
            uncheckedCast<Matcher<Any?>>(matcher)
                .test(resolver(value))
        }
        return MatcherResult(
            results.all { it.passed() },
            {
                results.map(MatcherResult::failureMessage)
                    .fold("") { acc: String, s: String -> acc + s + "\n" }
                    .trimIndent()
            },
            {
                results.map(MatcherResult::negatedFailureMessage)
                    .fold("") { acc: String, s: String -> acc + s + "\n" }
                    .trimIndent()
            },
        )
    }
}

private fun versionValidityMatcher(version: Int) = Matcher<Int> { value ->
    MatcherResult(
        version == value,
        { "Version $version should be $value" },
        { "Version $version should not be $value" },
    )
}

private fun variantValidityMatcher(variant: Int) = Matcher<Int> { value ->
    MatcherResult(
        variant == value,
        { "Variant $variant should be $value" },
        { "Variant $variant should not be $value" },
    )
}

private fun timestampValidityMatcher(timestamp: Long) = Matcher<Long> { value ->
    MatcherResult(
        timestamp == value,
        { "Timestamp $timestamp should be $value" },
        { "Timestamp $timestamp should not be $value" },
    )
}

private fun clockSequenceValidityMatcher(clockSequence: Short) = Matcher<Short> { value ->
    MatcherResult(
        clockSequence == value,
        { "Clock Sequence $clockSequence} should be $value" },
        { "Clock Sequence $clockSequence} should not be $value" },
    )
}

private fun nodeValidityMatcher(node: Long) = Matcher<Long> { value ->
    MatcherResult(
        node == value,
        { "Node $node should be $value" },
        { "Node $node should not be $value" },
    )
}

private class UuidEqualsMatcher(private val expected: UUID) : Matcher<UUID> {

    override fun test(value: UUID): MatcherResult {
        val failureMsg = buildString(DEFAULT_BUFFER_SIZE) {
            appendLine("Expected [$expected] but got [$value]")
            append(
                spaces(titleColumnLength), columnSeparator,
                "Expected".padCenter(contentColumnLength), columnSeparator,
                "Actual".padCenter(contentColumnLength), rowSeparator,
            )
            append(
                "Timestamp".padStart(titleColumnLength), columnSeparator,
                "%015X".format(expected.timestamp()).padEnd(contentColumnLength), columnSeparator,
                "%015X".format(value.timestamp()).padEnd(contentColumnLength), rowSeparator,
            )
            append(
                "Instant".padStart(titleColumnLength), columnSeparator,
                "%s".format(expected.instant).padEnd(contentColumnLength), columnSeparator,
                "%s".format(value.instant).padEnd(contentColumnLength), rowSeparator,
            )
            append(
                "Version".padStart(titleColumnLength), columnSeparator,
                "%01X".format(expected.version()).padEnd(contentColumnLength), columnSeparator,
                "%01X".format(value.version()).padEnd(contentColumnLength), rowSeparator,
            )
            append(
                "Variant".padStart(titleColumnLength), columnSeparator,
                "%04X".format(expected.variant() shl variantShift).padEnd(contentColumnLength), columnSeparator,
                "%04X".format(value.variant() shl variantShift).padEnd(contentColumnLength), rowSeparator,
            )
            append(
                "Clock sequence".padStart(titleColumnLength), columnSeparator,
                "%04X".format(expected.clockSequence()).padEnd(contentColumnLength), columnSeparator,
                "%04X".format(value.clockSequence()).padEnd(contentColumnLength), rowSeparator,
            )
            append(
                "Node".padStart(titleColumnLength), columnSeparator,
                "%012X".format(expected.node()).padEnd(contentColumnLength), columnSeparator,
                "%012X".format(value.node()).padEnd(contentColumnLength), rowSeparator,
            )
        }
        return MatcherResult(
            expected == value,
            { failureMsg },
            { failureMsg },
        )
    }

    private companion object {

        const val titleColumnLength = 16

        const val contentColumnLength = 20

        const val columnSeparator = " | "

        const val rowSeparator = " |\n"

        const val variantShift = 3
    }
}
