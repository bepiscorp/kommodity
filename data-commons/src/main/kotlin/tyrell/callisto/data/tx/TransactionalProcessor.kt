package tyrell.callisto.data.tx

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import tyrell.callisto.base.definition.DelicateLibraryApi
import tyrell.callisto.base.definition.LibraryApi
import java.io.Serializable

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi @DelicateLibraryApi
public interface TransactionalProcessor : Serializable {

    /**
     * Create a transaction from supplier and run it.
     *
     * @param supplier supplier
     *
     * @return supplier result
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public fun <T : Any?> process(supplier: (() -> T)): T
}

@Component
@Suppress("unused")
internal open class TransactionalProcessorImpl : TransactionalProcessor {

    override fun <T : Any?> process(supplier: () -> T): T = supplier()
}
