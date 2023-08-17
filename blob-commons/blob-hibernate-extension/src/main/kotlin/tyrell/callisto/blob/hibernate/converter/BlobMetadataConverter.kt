package tyrell.callisto.blob.hibernate.converter

import tyrell.callisto.blob.model.metadata.BlobMetadata
import tyrell.callisto.hibernate.converter.AbstractJsonConverter
import javax.persistence.Converter

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@Suppress("unused")
@Converter(autoApply = true)
internal class BlobMetadataConverter : AbstractJsonConverter<BlobMetadata>()
