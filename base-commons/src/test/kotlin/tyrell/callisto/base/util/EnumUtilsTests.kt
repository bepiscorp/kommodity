package tyrell.callisto.base.util

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.booleans.shouldNotBeTrue
import io.kotest.matchers.shouldBe

class EnumUtilsTests : FeatureSpec({

    feature("EnumUtils.readElement") {

        scenario("empty string should throw") {
            shouldThrow<IllegalArgumentException> {
                EnumUtils.readElement("", TestEnum::class.java)
            }
        }

        scenario("blank string should throw") {
            shouldThrow<IllegalArgumentException> {
                EnumUtils.readElement("   ", TestEnum::class.java)
            }
        }

        scenario("one word SCREAMING_CASE enum can be read") {
            EnumUtils.readElement("VALUE", TestEnum::class.java) shouldBe TestEnum.VALUE
        }

        scenario("two word SCREAMING_CASE enum can be read") {
            EnumUtils.readElement("PREFIX_POSTFIX", TestEnum::class.java) shouldBe TestEnum.PREFIX_POSTFIX
        }
    }

    feature("EnumUtils.containsElement") {

        scenario("null string should return false") {
            EnumUtils.containsElement(null, TestEnum::class.java).shouldNotBeTrue()
        }

        scenario("empty string should return false") {
            EnumUtils.containsElement("", TestEnum::class.java).shouldNotBeTrue()
        }

        scenario("blank string should return false") {
            EnumUtils.containsElement("   ", TestEnum::class.java).shouldNotBeTrue()
        }

        scenario("one word screaming SCREAMING_CASE enum can be checked") {
            EnumUtils.containsElement("VALUE", TestEnum::class.java).shouldBeTrue()
        }

        scenario("two word SCREAMING_CASE enum can be checked") {
            EnumUtils.containsElement("PREFIX_POSTFIX", TestEnum::class.java).shouldBeTrue()
        }
    }
},)

private enum class TestEnum {

    VALUE,

    PREFIX_POSTFIX;
}
