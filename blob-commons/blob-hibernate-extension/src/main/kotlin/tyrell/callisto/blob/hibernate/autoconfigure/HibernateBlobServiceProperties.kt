package tyrell.callisto.blob.hibernate.autoconfigure

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@ConstructorBinding
@ConfigurationProperties("callisto.blob-service.hibernate")
public data class HibernateBlobServiceProperties(

    val enabled: Boolean = false,
)
