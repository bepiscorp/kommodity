package tyrell.callisto.data.tx

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.runInterruptible
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tyrell.callisto.base.definition.ObsoleteLibraryApi
import java.io.Serializable
import kotlin.coroutines.CoroutineContext

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@ObsoleteCoroutinesApi
public interface SuspendingTransactionalProcessor : Serializable {

    /**
     * Create a transaction from closure and run it.
     *
     * @param closure closure to be run
     *
     * @return closure result
     */
    @ObsoleteLibraryApi
    public suspend fun <T : Any?> process(
        context: CoroutineContext? = null,
        closure: (suspend CoroutineScope.() -> T),
    ): T
}

@Suppress("unused")
@Component
internal open class SuspendingTransactionalProcessorImpl @Autowired constructor(

    private val transactionalProcessor: TransactionalProcessor,
) : SuspendingTransactionalProcessor {

    override suspend fun <T> process(context: CoroutineContext?, closure: (suspend CoroutineScope.() -> T)): T {
        val actualContext = if (context == null) {
            newSingleThreadContext("SuspendingTransactionalProcessor")
        } else {
            context + CoroutineName("SuspendingTransactionalProcessor")
        }

        return runInterruptible(actualContext) {
            transactionalProcessor.process {
                runBlocking {
                    closure()
                }
            }
        }
    }
}
