package tyrell.callisto.hibernate.converter

import tyrell.callisto.test.TestException
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class BooleanConverterTests {

    inner class ConvertToEntityAttributeTests {

        @Test
        fun `null resolves into null`() {
            assertNull(BooleanConverter().convertToEntityAttribute(null))
        }

        @Test
        fun `null str resolves into false`() {
            doTest("null") { assertFalse(it!!) }
        }

        @Test
        fun `T resolves into true`() {
            doTest("T") { assertTrue(it!!) }
        }

        @Test
        fun `F resolves into false`() {
            doTest("F") { assertFalse(it!!) }
        }

        private fun doTest(
            value: String?,
            resultChecker: ((Boolean?) -> Unit)? = null,
        ): Boolean? {
            val converter = BooleanConverter()
            val result = converter.convertToEntityAttribute(value)

            val me = "BooleanConverter.convertToEntityAttribute"
            assertNotNull(result) { "$me: Resolved null value for input value [$value]" }

            if (resultChecker != null) {
                try {
                    resultChecker.invoke(result)
                } catch (ex: Throwable) {
                    throw TestException("$me: Error happened converting value [$value] to entity attribute", ex)
                }
            }

            return result
        }
    }
}
