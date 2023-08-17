package tyrell.callisto.base.kotlin.properties

import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.booleans.shouldNotBeTrue
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.mockk.Called
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import tyrell.callisto.base.kotlin.dsl.uncheckedCast
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.PipedInputStream
import java.io.PipedOutputStream
import kotlin.concurrent.thread

class DelegatesTests : FeatureSpec({

    feature("transient lazy delegate") {

        scenario("value initialized when accessed the first time") {
            val valueFunc: () -> Int = mockk { every { this@mockk.invoke() } returns 42 }
            Delegates.transientLazy(valueFunc).run {
                isInitialized().shouldBeFalse()
                verify { valueFunc wasNot Called }

                value shouldBe 42
                isInitialized().shouldBeTrue()
                verify(exactly = 1) { valueFunc() }
            }
            confirmVerified(valueFunc)
        }

        scenario("delegate can be serialized") {
            ByteArrayOutputStream(DEFAULT_BUFFER_SIZE)
                .let(::ObjectOutputStream)
                .run {
                    writeObject(Delegates.transientLazy { 42 })
                    flush()
                }
        }

        scenario("delegate can be deserialized") {
            val lazyDelegate: Lazy<Int> = Delegates.transientLazy { 42 }
            val objectInStream: ObjectInputStream = run {
                val pipedOutStream = PipedOutputStream()
                val pipedInStream = PipedInputStream()
                pipedOutStream.connect(pipedInStream)
                thread(start = true) {
                    ObjectOutputStream(pipedOutStream).run {
                        writeObject(lazyDelegate)
                        flush()
                    }
                }
                ObjectInputStream(pipedInStream)
            }
            objectInStream.readObject().run {
                should { it is Lazy<*> }
                check(this is Lazy<*>)
                isInitialized().shouldNotBeTrue()
                value shouldBe 42
                isInitialized().shouldBeTrue()
            }
        }

        scenario("value reinitialized when accessed the first time after deserialization") {
            val lazyDelegate: Lazy<Int> = Delegates.transientLazy { 42 }.apply {
                isInitialized().shouldBeFalse()
                value shouldBe 42
                isInitialized().shouldBeTrue()
            }
            val objectInStream: ObjectInputStream = run {
                val pipedOutStream = PipedOutputStream()
                val pipedInStream = PipedInputStream()
                pipedOutStream.connect(pipedInStream)
                thread(start = true) {
                    ObjectOutputStream(pipedOutStream).run {
                        writeObject(lazyDelegate)
                        flush()
                    }
                }
                ObjectInputStream(pipedInStream)
            }
            uncheckedCast<Lazy<Int>>(objectInStream.readObject()).apply {
                isInitialized().shouldNotBeTrue()
                value shouldBe 42
                isInitialized().shouldBeTrue()
            }
        }
    }
},)
