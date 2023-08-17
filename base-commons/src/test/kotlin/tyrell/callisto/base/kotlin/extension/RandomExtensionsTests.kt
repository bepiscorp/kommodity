package tyrell.callisto.base.kotlin.extension

import io.kotest.core.spec.style.StringSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import kotlin.random.Random

class RandomExtensionsTests : StringSpec({

    "Random.nextEnum returns pseudo random value" {
        forAll(
            row(0xDEAD, TestEnum.FIRST_VALUE),
            row(0xDEDD, TestEnum.SECOND_VALUE),
            row(0xAABB, TestEnum.THIRD_VALUE),
            row(0xBABA, TestEnum.FOURTH_VALUE),
        ) { seed, expectedValue -> Random(seed).nextEnum(TestEnum::class.java) shouldBe expectedValue }
    }

    "Generic Random.nextEnum returns pseudo random value" {
        forAll(
            row(0xDEAD, TestEnum.FIRST_VALUE),
            row(0xDEDD, TestEnum.SECOND_VALUE),
            row(0xAABB, TestEnum.THIRD_VALUE),
            row(0xBABA, TestEnum.FOURTH_VALUE),
        ) { seed, expectedValue -> Random(seed).nextEnum<TestEnum>() shouldBe expectedValue }
    }
},)

private enum class TestEnum {

    FIRST_VALUE,

    SECOND_VALUE,

    THIRD_VALUE,

    FOURTH_VALUE;
}
