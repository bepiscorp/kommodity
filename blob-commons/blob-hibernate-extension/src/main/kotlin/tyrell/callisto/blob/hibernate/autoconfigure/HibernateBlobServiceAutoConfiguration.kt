package tyrell.callisto.blob.hibernate.autoconfigure

import mu.KLogger
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import tyrell.callisto.base.kotlin.properties.LoggingDelegates
import tyrell.callisto.blob.hibernate.dao.jpa.BlobEntityDao
import tyrell.callisto.blob.hibernate.service.HibernateBlobService
import tyrell.callisto.blob.service.BlobService

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@Configuration
@ConditionalOnProperty(
    prefix = "callisto.blob-service.hibernate", name = ["enabled"],
    havingValue = "true", matchIfMissing = false,
)
@EnableConfigurationProperties(HibernateBlobServiceProperties::class)
public open class HibernateBlobServiceAutoConfiguration {

    private val logger: KLogger by LoggingDelegates.instanceLogger()

    @Bean
    public open fun hibernateBlobService(
        blobEntityDao: BlobEntityDao,
        properties: HibernateBlobServiceProperties,
    ): BlobService = HibernateBlobService(
        blobEntityDao = blobEntityDao,
    )
}
