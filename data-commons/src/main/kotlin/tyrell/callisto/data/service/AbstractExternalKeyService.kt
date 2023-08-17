package tyrell.callisto.data.service

import org.springframework.beans.factory.annotation.Autowired
import tyrell.callisto.base.definition.LibraryApi
import tyrell.callisto.base.type.ExternalKey
import tyrell.callisto.base.type.ExternalKeyView
import tyrell.callisto.data.dao.ExternalKeyDao
import java.io.Serializable

/**
 * Abstract class [AbstractExternalKeyService] provides base implementations of [ExternalKeyService] declarations.
 *
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public abstract class AbstractExternalKeyService<
    L : Serializable,
    K : ExternalKey<L>,
    DAO : ExternalKeyDao<L, K>,
    > : ExternalKeyService<L> {

    @set:Autowired
    public lateinit var dao: DAO

    override fun findLastByExternalKey(externalKeyView: ExternalKeyView): L? =
        dao.findLastByExternalKey(externalKeyView)

    override fun findByExternalKey(externalKeyView: ExternalKeyView): List<L> =
        dao.findByExternalKey(externalKeyView)

    override fun save(externalKeys: List<ExternalKeyView>, localId: L): List<ExternalKey<L>> =
        dao.save(externalKeys, localId)

    override fun save(externalKeyView: ExternalKeyView, localId: L): ExternalKey<L> =
        dao.save(externalKeyView, localId)
}
