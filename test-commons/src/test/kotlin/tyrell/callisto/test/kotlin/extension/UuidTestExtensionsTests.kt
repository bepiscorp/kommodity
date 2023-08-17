package tyrell.callisto.test.kotlin.extension

import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.uuids.shouldBe
import tyrell.callisto.base.kotlin.extension.clockSequence
import tyrell.callisto.base.kotlin.extension.instant
import tyrell.callisto.base.kotlin.extension.node
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.UUID

class UuidTestExtensionsTests : FeatureSpec({
    feature("Int.toUuidTimestamp extension") {
        scenario("zero value resolves resolves relatively to base time") {
            val timestampUuid = 0.toUuidTimestamp()
            timestampUuid.instant shouldBe LocalDateTime.of(
                2019, 11,
                15, 10, 0,
            ).toInstant(ZoneOffset.UTC)
            timestampUuid shouldBe UUID.fromString("af899000-078e-11ea-8000-0000deadbeef")
        }
        scenario("non-zero value resolves into timestamp") {
            val offset = 315_360_000 // 3650 days
            val timestampUuid = offset.toUuidTimestamp()
            timestampUuid.instant shouldBe LocalDateTime.of(
                2029, 11, 12,
                10, 0,
            ).toInstant(ZoneOffset.UTC)
            timestampUuid shouldBe UUID.fromString("674d1000-3bbd-11f5-8000-0000deadbeef")
        }
    }

    feature("Long.toUuidNode extension") {
        scenario("zero value resolved into zero Node ID in the UUID") {
            val nodeUuid = 0L.toUuidNode()
            nodeUuid.node shouldBe 0L
            nodeUuid shouldBe UUID.fromString("af899000-078e-11ea-8000-000000000000")
        }
        scenario("non-zero value resolved into Node ID in the UUID") {
            val nodeUuid = 0xDEDA_BEBA_DEAD.toUuidNode()
            nodeUuid.node shouldBe 0xDEDA_BEBA_DEAD
            nodeUuid shouldBe UUID.fromString("af899000-078e-11ea-8000-dedabebadead")
        }
    }

    feature("Short.toUuidClockSeq extension") {
        scenario("zero value resolved into zero Clock Seq in UUID") {
            val clockSeqUuid = 0.toShort().toUuidClockSeq()
            clockSeqUuid.clockSequence shouldBe 0
            clockSeqUuid shouldBe UUID.fromString("af899000-078e-11ea-8000-0000deadbeef")
        }
        scenario("non-zero value resolved into Clock Seq in UUID") {
            val clockSeqUuid = 0x3FFF.toShort().toUuidClockSeq()
            clockSeqUuid.clockSequence shouldBe 0x3FFF
            clockSeqUuid shouldBe UUID.fromString("af899000-078e-11ea-bfff-0000deadbeef")
        }
    }
},)
