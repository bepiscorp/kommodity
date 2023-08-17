package tyrell.callisto.data.dao

import org.springframework.stereotype.Repository
import tyrell.callisto.base.definition.LibraryApi
import tyrell.callisto.base.type.ExternalKey
import tyrell.callisto.base.type.ExternalKeyView
import java.io.Serializable

/**
 * The [ExternalKeyDao] class implements generic data access operations for subclasses of [ExternalKey].
 *
 * @param [L] the class of local identifier of external key entity representing details view
 * @param [K] the class of external key entity
 *
 * @author Mikhail Gostev
 * @since 0.0.1
 * @see ExternalKey
 **/
@LibraryApi
@Repository
public interface ExternalKeyDao<L : Serializable, K : ExternalKey<L>> {

    /**
     * Finds last local id by external key view object.
     *
     * @param externalKey external key view object
     *
     * @return optional of last local id
     */
    public fun findLastByExternalKey(externalKey: ExternalKeyView): L?

    /**
     * Finds list of local indices by external key view object.
     *
     * @param externalKey external key view object
     *
     * @return list of local indices
     */
    public fun findByExternalKey(externalKey: ExternalKeyView): List<L>

    /**
     * Saves new external key entity received from external key view object and local id.
     *
     * @param externalKey external key view object
     * @param localId       local id
     *
     * @return saved external key entity
     *
     * @see ExternalKeyDao.convertToExternalKey
     */
    public fun save(externalKey: ExternalKeyView, localId: L): ExternalKey<L>

    /**
     * Saves list of new external key entities received from list of external key view objects and same local id.
     *
     * @param externalKeys list of external key view objects
     * @param localId           local id
     *
     * @return list of saved external key entities
     *
     * @see ExternalKeyDao.convertToExternalKey
     */
    public fun save(externalKeys: List<ExternalKeyView>, localId: L): List<K>

    /**
     * Converts from external key view object and local id to external key entity,
     * which is subclasses by abstract ExternalKey.
     *
     * @param externalKey external key view object
     * @param localId       local id
     *
     * @return external key entity
     */
    public fun convertToExternalKey(externalKey: ExternalKeyView, localId: L): K
}
