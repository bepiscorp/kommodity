package tyrell.callisto.data.liquibase

import io.kotest.core.spec.style.FunSpec
import io.kotest.core.spec.style.scopes.ContainerScope
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe

class LogicalResourceComparatorTests : FunSpec({

    val comparator: Comparator<String> = LogicalResourceComparator()

    data class ComparatorTest(
        val first: String,
        val second: String,
        val expected: Int,
    )

    val testComparator: suspend ContainerScope.(ComparatorTest) -> Unit = { test ->
        val (first, second, expected) = test
        comparator.compare(first, second) shouldBe expected
        comparator.compare(second, first) shouldBe -expected
    }

    context("versions compared logically") {
        withData(
            ComparatorTest("1.0.0", "1.0.0", 0),
            ComparatorTest("2.0.0", "1.0.0", 1),
            ComparatorTest("3.0.0", "1.0.0", 1),
            ComparatorTest("10.0.0", "1.0.0", 1),
            test = testComparator,
        )
    }

    context("words compared lexicographically") {
        withData(
            ComparatorTest("callisto", "callisto", 0),
            ComparatorTest("castor", "callisto", 7),
            ComparatorTest("naos", "callisto", 11),
            ComparatorTest("naos", "castor", 11),
            test = testComparator,
        )
    }

    context("paths compared logically and lexicographically") {
        withData(
            ComparatorTest(
                "tyrell/castor/1.0.0/0-callisto-init-schema.changelog.xml",
                "tyrell/castor/1.0.0/1-init-schema.changelog.xml",
                -1,
            ),
            ComparatorTest(
                "tyrell/castor/1.0.0/0-callisto-init-schema.changelog.xml",
                "tyrell/castor/1.0.1/0-callisto-init-schema.changelog.xml",
                -1,
            ),
            ComparatorTest(
                "tyrell/castor/1.0.0/0-callisto-init-schema.changelog.xml",
                "tyrell/castor/10.0.0/0-callisto-init-schema.changelog.xml",
                -1,
            ),
            test = testComparator,
        )
    }
},)
