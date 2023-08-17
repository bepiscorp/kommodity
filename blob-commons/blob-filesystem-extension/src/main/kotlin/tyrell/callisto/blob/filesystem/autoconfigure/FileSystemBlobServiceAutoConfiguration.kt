package tyrell.callisto.blob.filesystem.autoconfigure

import mu.KLogger
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.system.ApplicationTemp
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import tyrell.callisto.base.kotlin.properties.LoggingDelegates
import tyrell.callisto.blob.filesystem.service.FileSystemBlobService
import tyrell.callisto.blob.service.BlobService
import java.nio.file.Path
import java.nio.file.attribute.PosixFilePermission
import java.nio.file.attribute.PosixFilePermissions
import java.util.EnumSet
import kotlin.io.path.createDirectories
import kotlin.io.path.exists
import kotlin.io.path.isDirectory
import kotlin.io.path.setPosixFilePermissions

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@Configuration
@ConditionalOnProperty(
    prefix = "callisto.blob-service.file-system", name = ["enabled"],
    havingValue = "true", matchIfMissing = false,
)
@EnableConfigurationProperties(FileSystemBlobServiceProperties::class)
public open class FileSystemBlobServiceAutoConfiguration {

    private val logger: KLogger by LoggingDelegates.instanceLogger()

    private val newBlobDirectoryAttributes: Set<PosixFilePermission> = EnumSet.of(
        PosixFilePermission.OWNER_READ,
        PosixFilePermission.OWNER_WRITE,
        PosixFilePermission.OWNER_EXECUTE,
    )

    @Bean
    public open fun fileSystemBlobService(properties: FileSystemBlobServiceProperties): BlobService =
        FileSystemBlobService(
            blobDirectory = resolveBlobDirectory(properties),
            deleteOnExit = properties.deleteOnExit,
        )

    private fun resolveBlobDirectory(properties: FileSystemBlobServiceProperties): Path {
        val directory: Path? = properties.directory
        if (directory == null) {
            if (!properties.createIfAbsent) throw IllegalStateException(
                """
                   Parameter [callisto.blob-service.file-system.directory] was not defined.
                   Consider defining directory or setting [callisto.blob-service.file-system.createIfAbsent] to true
                """.trimIndent().trimMargin(),
            ).let(logger::throwing)

            return ApplicationTemp(FileSystemBlobService::class.java).dir.toPath().also {
                it.setPosixFilePermissions(newBlobDirectoryAttributes)
                logger.trace { "Created temporary directory [$it]" }
            }
        }

        val exists: Boolean = directory.exists()
        if (!exists) {
            if (!properties.createIfAbsent) throw IllegalStateException(
                "Parameter [callisto.blob-service.file-system.directory] was defined" +
                    " but corresponding directory haven't existed." +
                    " Consider creating defined directory" +
                    " or setting [callisto.blob-service.file-system.createIfAbsent] to true",
            ).let(logger::throwing)

            directory.createDirectories(PosixFilePermissions.asFileAttribute(newBlobDirectoryAttributes))
            logger.trace { "Created defined directory [$directory]" }

            return directory
        }

        if (!directory.isDirectory()) throw IllegalStateException(
            "Filesystem Blob service has failed to initialize as Blob directory [$directory] appeared to be a file.",
        ).let(logger::throwing)

        if (properties.deleteOnExit) {
            logger.info {
                "Filesystem Blob directory [$directory] will be deleted on application completion " +
                    "as parameter [callisto.blob-service.file-system.deleteOnExit] is true"
            }
        }

        return directory.toAbsolutePath()
    }
}
