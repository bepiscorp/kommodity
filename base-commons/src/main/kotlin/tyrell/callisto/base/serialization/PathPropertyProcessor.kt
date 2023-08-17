package tyrell.callisto.base.serialization

import com.fasterxml.jackson.databind.DeserializationContext
import tyrell.callisto.base.definition.ExperimentalLibraryApi
import java.io.File
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.readBytes

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@ExperimentalLibraryApi
public class PathPropertyProcessor : ExternalValueProcessorBase {

    private val rootDirectory: Path?

    public constructor() : super(PREFIX) {
        this.rootDirectory = null
    }

    public constructor(rootDirectory: Path) : super(PREFIX) {
        this.rootDirectory = rootDirectory
    }

    override fun process(
        value: String,
        mandatory: Boolean,
        context: DeserializationContext,
        additionalConfigValues: Map<String, Any>,
    ): Any? {
        require(mandatory) { "Non mandatory values are not supported for [FilePropertyProcessor]" }

        val rootPrefix: String = getRootDirectory(context).absolutePathString()
        val resourcePath: String = getResourcePath(value)
        val pathString: String = if (resourcePath.startsWith(File.separatorChar)) {
            rootPrefix + resourcePath
        } else {
            rootPrefix + File.separatorChar + resourcePath
        }

        return resolveProcessedValue(pathString, context)
    }

    private fun resolveProcessedValue(pathString: String, context: DeserializationContext): Any? {
        val expectedClass: Class<*>? = context.contextualType?.rawClass // TODO: Contextual type seems to never present
        return when {
            (expectedClass == Path::class.java) -> Path(pathString)
            (expectedClass == null) -> Path(pathString)

            (expectedClass == ByteArray::class.java) -> Path(pathString).readBytes()

            (expectedClass == String::class.java) -> pathString
            else -> pathString
        }
    }

    private fun getRootDirectory(context: DeserializationContext): Path {
        if (this.rootDirectory != null) {
            return this.rootDirectory
        }

        val rootDirectory: Path? = context.getAttribute(ROOT_FOLDER_PATH_ATTRIBUTE) as Path?
        if (rootDirectory != null) {
            return rootDirectory
        }

        return Path("")
    }

    public companion object {

        public const val PREFIX: String = "path"

        /**
         * Config key of root folder. File paths will be resolved opposite to root folder.
         * Default value is current directory (but it is not guaranteed).
         */
        public val ROOT_FOLDER_PATH_ATTRIBUTE: String = "__${PathPropertyProcessor::class.simpleName}#rootFolder"
    }
}
