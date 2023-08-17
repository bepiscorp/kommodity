package tyrell.callisto.blob.hibernate.service

import kotlinx.coroutines.runInterruptible
import org.springframework.data.repository.findByIdOrNull
import tyrell.callisto.base.definition.InternalLibraryApi
import tyrell.callisto.blob.enums.BlobState
import tyrell.callisto.blob.hibernate.dao.jpa.BlobEntityDao
import tyrell.callisto.blob.hibernate.entity.BlobEntity
import tyrell.callisto.blob.model.BlobModel
import tyrell.callisto.blob.model.ByteArrayBinaryData
import tyrell.callisto.blob.model.IdentifierData
import tyrell.callisto.blob.model.readBytesBlocking
import tyrell.callisto.blob.service.impl.AbstractBlobService
import java.util.UUID

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@InternalLibraryApi
public class HibernateBlobService(

    private val blobEntityDao: BlobEntityDao,
) : AbstractBlobService() {

    override suspend fun doUpload(model: BlobModel): Unit = runInterruptible closure@{
        val entity: BlobEntity = model.toEntity()
        blobEntityDao.saveAndFlush(entity)
    }

    override suspend fun doLoad(identifier: IdentifierData): BlobModel? = runInterruptible closure@{
        val id: UUID = checkNotNull(identifier.id) { "Null id was provided for blob loading" }

        val entity: BlobEntity = blobEntityDao.findByIdOrNull(id) ?: return@closure null

        return@closure entity.toModel()
    }

    private fun BlobModel.toEntity(): BlobEntity = BlobEntity(
        data = data.readBytesBlocking(),
        metadata = metadata,
        id = checkNotNull(identifier.id),
    )

    private fun BlobEntity.toModel(): BlobModel = BlobModel(
        data = ByteArrayBinaryData(checkNotNull(data)),
        state = BlobState.UPLOADED,
        identifier = IdentifierData(id = id),
        metadata = checkNotNull(metadata),
    )
}
