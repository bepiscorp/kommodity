package tyrell.callisto.base.kotlin.extension

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.data.row
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe

class StringExtensionsTests : FunSpec({

    context("(CharSequence | String).padCenter") {

        fun String.asCharSequence(): CharSequence = this

        data class PadCenterTest(
            val input: CharSequence,
            val expectedResult: String,
            val length: Int,
            val padChar: Char = '▲',
        )

        test("negative length should throw IllegalArgumentException") {
            shouldThrow<IllegalArgumentException> {
                "".asCharSequence().padCenter(length = -1)
                "".padCenter(length = -1)

                "abc".asCharSequence().padCenter(length = -1)
                "abc".padCenter(length = -1)
            }
        }

        test("zero length should not throw") {
            shouldNotThrowAny {
                "abc".asCharSequence().padCenter(length = 0)
                "abc".padCenter(length = 0)
            }
        }

        withData(
            nameFn = { (input, expected, length, padChar) ->
                "\"$input\".padCenter(length = $length, padChar='$padChar') == \"$expected\""
            },
            PadCenterTest("", "", length = 0),
            PadCenterTest("", "▲▲▲▲▲", length = 5),

            PadCenterTest("ab", "ab", length = 0),
            PadCenterTest("ab", "ab", length = 1),
            PadCenterTest("abc", "abc", length = 0),
            PadCenterTest("abc", "abc", length = 2),

            PadCenterTest("ab", "▲ab▲", length = 4),
            PadCenterTest("abc", "▲abc▲", length = 5),
            PadCenterTest("ab", "▲▲ab▲▲", length = 6),
            PadCenterTest("abc", "▲▲▲abc▲▲▲", length = 9),

            PadCenterTest("ab", "ab▲", length = 3),
            PadCenterTest("abc", "abc▲", length = 4),
            PadCenterTest("ab", "▲ab▲▲", length = 5),
            PadCenterTest("abc", "▲abc▲▲", length = 6),
        ) { (input, expected, length, padChar) ->
            input.padCenter(length, padChar).toString() shouldBe expected
            input.toString().padCenter(length, padChar) shouldBe expected
        }
    }

    context("spaces() function") {
        test("negative length should throw IllegalArgumentException") {
            shouldThrow<IllegalArgumentException> {
                spaces(n = -1)
                spaces(n = -10)
            }
        }
        withData(
            nameFn = { (times, expected) -> "spaces(n = $times) == \"$expected\"" },
            row(0, ""),
            row(1, " "),
            row(2, "  "),
        ) { (times, expected) ->
            spaces(n = times) shouldBe expected
        }
    }

    context("Char.repeat() function") {
        test("negative length should throw IllegalArgumentException") {
            shouldThrow<IllegalArgumentException> {
                'X'.repeat(n = -1)
                'X'.repeat(n = -10)
            }
        }
        withData(
            nameFn = { (input, times, expected) ->
                "'$input'.repeat(n = $times) == \"$expected\""
            },
            row('▲', 0, ""),
            row('▲', 1, "▲"),
            row('▲', 2, "▲▲"),
            row('▲', 5, "▲▲▲▲▲"),
        ) { (input: Char, times, expected) ->
            input.repeat(times) shouldBe expected
        }
    }
},)
