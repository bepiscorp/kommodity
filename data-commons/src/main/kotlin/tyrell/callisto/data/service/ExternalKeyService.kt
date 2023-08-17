package tyrell.callisto.data.service

import org.springframework.stereotype.Service
import tyrell.callisto.base.definition.LibraryApi
import tyrell.callisto.base.type.ExternalKey
import tyrell.callisto.base.type.ExternalKeyView
import java.io.Serializable

/**
 * [ExternalKeyService] interface defines basic operations on [ExternalKey] entity.
 *
 * @param <D> the class of entity representing details view
 * @param <S> the class of entity representing summary view
 *
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
@Service
public interface ExternalKeyService<L : Serializable> {

    /**
     * Searches for the local id by ExternalKeyVo.
     *
     * @param externalKeyView external key view object
     *
     * @return local id
     */
    public fun findLastByExternalKey(externalKeyView: ExternalKeyView): L?

    /**
     * Searches for list of local indices by ExternalKeyVo.
     *
     * @param externalKeyView external key view object
     *
     * @return list of local indices
     */
    public fun findByExternalKey(externalKeyView: ExternalKeyView): List<L>

    /**
     * Save the list of external view object which refer to same local id.
     *
     * @param externalKeys external key view objects
     * @param localId      local id
     *
     * @return list of saved external keys
     */
    public fun save(externalKeys: List<ExternalKeyView>, localId: L): List<ExternalKey<L>>

    /**
     * Save external view object which refer to local id.
     *
     * @param externalKeyView external key view object
     * @param localId       local id
     *
     * @return saved external key
     */
    public fun save(externalKeyView: ExternalKeyView, localId: L): ExternalKey<L>
}
