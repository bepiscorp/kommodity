package tyrell.callisto.base.kotlin.extension

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.uuids.shouldBe
import io.kotest.matchers.uuids.shouldBeValid
import io.kotest.property.Arb
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.instant
import io.kotest.property.arbitrary.long
import io.kotest.property.arbitrary.short
import io.kotest.property.checkAll
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.UUID

class UuidExtensionsTests : FunSpec({

    context("timeBasedUuid() function") {

        fun instant(
            year: Int,
            month: Int,
            day: Int,
            hour: Int = 0,
            minute: Int = 0,
            second: Int = 0,
            nanoOfSecond: Int = 0,
        ) = LocalDateTime.of(year, month, day, hour, minute, second, nanoOfSecond).toInstant(ZoneOffset.UTC)

        fun doTest(
            instant: Instant,
            node: Long,
            clockSequence: Short,
            expectedResult: UUID? = null,
        ): UUID = timeBasedUuid(
            instant = instant,
            node = node,
            clockSequence = clockSequence,
        ).also { uuid ->
            uuid.shouldBeValid()
            uuid.instant shouldBe instant
            uuid.node shouldBe node
            uuid.clockSequence shouldBe clockSequence
            if (expectedResult != null) {
                uuid shouldBe expectedResult
            }
        }

        test("runs successfully when all arguments defined") {
            doTest(
                instant = instant(2022, 2, 1, 10, 0),
                node = 123,
                clockSequence = 25,
            )
        }

        test("runs successfully when instant has nano precision") {
            doTest(
                instant = instant(
                    3107, 10, 2,
                    4, 2, 49,
                    nanoOfSecond = 246294900,
                ),
                node = 0,
                clockSequence = 0,
            )
        }

        test("fails when instant nanos is too precise") {
            shouldThrow<IllegalArgumentException> {
                timeBasedUuid(
                    instant = instant(
                        3107, 10, 2,
                        4, 2, 49,
                        nanoOfSecond = 99,
                    ),
                )
            }
        }

        context("property tests") {

            threads = Runtime.getRuntime().availableProcessors()

            fun allInstants() = Arb.instant(Instant.EPOCH..UUID_MAX_INSTANT).filter { it.nano % 100 == 0 }
            fun allNodes() = Arb.long(0, UUID_MAX_NODE)
            fun allClockSequences() = Arb.short(0, UUID_MAX_CLOCK_SEQUENCE)

            test("valid uuid generated for all values and instant with millis precision") {
                checkAll(
                    iterations = 100,
                    allInstants().filter { it.nano % 1_000_000 == 0 },
                    allNodes(),
                    allClockSequences(),
                ) { instant, node, clockSeq -> doTest(instant, node, clockSeq) }
            }

            test("valid uuid generated for all values and instant with nanos precision") {
                checkAll(
                    iterations = 1000,
                    allInstants(),
                    allNodes(),
                    allClockSequences(),
                ) { instant, node, clockSeq -> doTest(instant, node, clockSeq) }
            }
        }
    }
},)
