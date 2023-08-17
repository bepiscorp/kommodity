package tyrell.callisto.blob.filesystem.autoconfigure

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import java.nio.file.Path

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@ConstructorBinding
@ConfigurationProperties("callisto.blob-service.file-system")
public data class FileSystemBlobServiceProperties(

    val enabled: Boolean = false,

    val directory: Path? = null,

    val createIfAbsent: Boolean = true,

    val deleteOnExit: Boolean = false,
)
