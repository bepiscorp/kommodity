package tyrell.callisto.data.serialization

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.core.Base64Variant
import com.fasterxml.jackson.core.FormatSchema
import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.PrettyPrinter
import com.fasterxml.jackson.core.TreeNode
import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.core.io.CharacterEscapes
import com.fasterxml.jackson.core.type.ResolvedType
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.AnnotationIntrospector
import com.fasterxml.jackson.databind.DeserializationConfig
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.InjectableValues
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.MappingIterator
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ObjectReader
import com.fasterxml.jackson.databind.ObjectWriter
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.SerializationConfig
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.cfg.ContextAttributes
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator
import com.fasterxml.jackson.databind.cfg.MapperBuilder
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler
import com.fasterxml.jackson.databind.introspect.ClassIntrospector
import com.fasterxml.jackson.databind.introspect.VisibilityChecker
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper
import com.fasterxml.jackson.databind.jsontype.NamedType
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider
import com.fasterxml.jackson.databind.ser.FilterProvider
import com.fasterxml.jackson.databind.ser.SerializerFactory
import com.fasterxml.jackson.databind.type.TypeFactory
import tyrell.callisto.base.definition.ExperimentalLibraryApi
import tyrell.callisto.base.definition.InternalLibraryApi
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.io.Reader
import java.io.Writer
import java.lang.reflect.Type
import java.net.URL
import java.text.DateFormat
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.atomic.AtomicReference

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@ExperimentalLibraryApi
@Suppress("NOTHING_TO_INLINE")
public inline fun <B : MapperBuilder<*, B>> B.buildImmutable(name: String): ObjectMapper =
    ImmutableObjectMapper(name, build())

/**
 * The [ImmutableObjectMapper] class.
 *
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@InternalLibraryApi
@Suppress("TooManyFunctions")
public class ImmutableObjectMapper @PublishedApi internal constructor(
    name: String,
    private val delegate: ObjectMapper,
) : ObjectMapper() {

    public val name: String = name.also { check(it.isNotBlank()) }

    override fun disable(vararg features: JsonGenerator.Feature): Nothing = throw UnsupportedOperationException()

    override fun enable(vararg features: JsonGenerator.Feature): Nothing = throw UnsupportedOperationException()

    override fun configure(f: JsonGenerator.Feature, state: Boolean): Nothing = throw UnsupportedOperationException()

    override fun disable(vararg features: JsonParser.Feature): Nothing = throw UnsupportedOperationException()

    override fun enable(vararg features: JsonParser.Feature): Nothing = throw UnsupportedOperationException()

    override fun configure(feature: JsonParser.Feature, state: Boolean): Nothing = throw UnsupportedOperationException()

    override fun disable(first: DeserializationFeature, vararg f: DeserializationFeature): Nothing =
        throw UnsupportedOperationException()

    override fun disable(feature: DeserializationFeature): Nothing = throw UnsupportedOperationException()

    override fun enable(first: DeserializationFeature, vararg f: DeserializationFeature): Nothing =
        throw UnsupportedOperationException()

    override fun enable(feature: DeserializationFeature): Nothing = throw UnsupportedOperationException()

    override fun configure(f: DeserializationFeature, state: Boolean): Nothing = throw UnsupportedOperationException()

    override fun disable(first: SerializationFeature, vararg f: SerializationFeature): Nothing =
        throw UnsupportedOperationException()

    override fun disable(f: SerializationFeature): Nothing = throw UnsupportedOperationException()

    override fun enable(first: SerializationFeature, vararg f: SerializationFeature): Nothing =
        throw UnsupportedOperationException()

    override fun enable(f: SerializationFeature): Nothing = throw UnsupportedOperationException()

    override fun configure(f: SerializationFeature, state: Boolean): Nothing = throw UnsupportedOperationException()

    override fun disable(vararg f: MapperFeature): Nothing = throw UnsupportedOperationException()

    override fun enable(vararg f: MapperFeature): Nothing = throw UnsupportedOperationException()

    override fun configure(f: MapperFeature, state: Boolean): Nothing = throw UnsupportedOperationException()

    override fun setTimeZone(tz: TimeZone): Nothing = throw UnsupportedOperationException()

    override fun setLocale(l: Locale): Nothing = throw UnsupportedOperationException()

    override fun getInjectableValues(): Nothing = throw UnsupportedOperationException()

    override fun setInjectableValues(injectableValues: InjectableValues): Nothing =
        throw UnsupportedOperationException()

    override fun setHandlerInstantiator(hi: HandlerInstantiator): Nothing = throw UnsupportedOperationException()

    override fun getDateFormat(): Nothing = throw UnsupportedOperationException()

    override fun setDateFormat(dateFormat: DateFormat): Nothing = throw UnsupportedOperationException()

    override fun getJsonFactory(): Nothing = throw UnsupportedOperationException()

    override fun getFactory(): Nothing = throw UnsupportedOperationException()

    override fun setConfig(config: SerializationConfig): Nothing = throw UnsupportedOperationException()

    override fun setBase64Variant(v: Base64Variant): Nothing = throw UnsupportedOperationException()

    override fun setFilterProvider(filterProvider: FilterProvider): Nothing = throw UnsupportedOperationException()

    override fun setFilters(filterProvider: FilterProvider): Nothing = throw UnsupportedOperationException()

    override fun setConfig(config: DeserializationConfig): Nothing = throw UnsupportedOperationException()

    override fun clearProblemHandlers(): Nothing = throw UnsupportedOperationException()

    override fun addHandler(handler: DeserializationProblemHandler): Nothing = throw UnsupportedOperationException()

    override fun setNodeFactory(factory: JsonNodeFactory): Nothing = throw UnsupportedOperationException()

    override fun getNodeFactory(): Nothing = throw UnsupportedOperationException()

    override fun constructType(type: Type): Nothing = throw UnsupportedOperationException()

    override fun setTypeFactory(factory: TypeFactory): Nothing = throw UnsupportedOperationException()

    override fun registerSubtypes(vararg types: NamedType): Nothing = throw UnsupportedOperationException()

    override fun registerSubtypes(vararg classes: Class<*>?): Nothing = throw UnsupportedOperationException()

    override fun setDefaultTyping(typer: TypeResolverBuilder<*>?): Nothing = throw UnsupportedOperationException()

    override fun disableDefaultTyping(): Nothing = throw UnsupportedOperationException()

    override fun enableDefaultTypingAsProperty(
        applicability: DefaultTyping,
        propertyName: String,
    ): Nothing = throw UnsupportedOperationException()

    override fun enableDefaultTyping(
        applicability: DefaultTyping,
        includeAs: JsonTypeInfo.As,
    ): Nothing = throw UnsupportedOperationException()

    override fun enableDefaultTyping(typing: DefaultTyping): Nothing = throw UnsupportedOperationException()

    override fun enableDefaultTyping(): Nothing = throw UnsupportedOperationException()

    override fun setDefaultPrettyPrinter(printer: PrettyPrinter): Nothing = throw UnsupportedOperationException()

    override fun setSerializationInclusion(
        include: JsonInclude.Include,
    ): Nothing = throw UnsupportedOperationException()

    override fun getPropertyNamingStrategy(): Nothing = throw UnsupportedOperationException()

    override fun setPropertyNamingStrategy(
        strategy: PropertyNamingStrategy,
    ): Nothing = throw UnsupportedOperationException()

    override fun setAnnotationIntrospectors(
        serializerAI: AnnotationIntrospector,
        deserializerAI: AnnotationIntrospector,
    ): Nothing = throw UnsupportedOperationException()

    override fun setAnnotationIntrospector(
        introspector: AnnotationIntrospector,
    ): Nothing = throw UnsupportedOperationException()

    override fun setSubtypeResolver(resolver: SubtypeResolver): Nothing = throw UnsupportedOperationException()

    override fun getSubtypeResolver(): Nothing = throw UnsupportedOperationException()

    override fun setVisibility(
        forMethod: PropertyAccessor,
        visibility: JsonAutoDetect.Visibility,
    ): Nothing = throw UnsupportedOperationException()

    override fun setVisibility(checker: VisibilityChecker<*>?): Nothing = throw UnsupportedOperationException()

    override fun getVisibilityChecker(): Nothing = throw UnsupportedOperationException()

    override fun setVisibilityChecker(checker: VisibilityChecker<*>?): Nothing = throw UnsupportedOperationException()

    override fun setMixInAnnotations(sourceMixins: Map<Class<*>?, Class<*>?>?) {
        throw UnsupportedOperationException()
    }

    override fun mixInCount(): Nothing = throw UnsupportedOperationException()

    override fun findMixInClassFor(cls: Class<*>?): Class<*> {
        throw UnsupportedOperationException()
    }

    override fun setMixInResolver(resolver: ClassIntrospector.MixInResolver): Nothing =
        throw UnsupportedOperationException()

    override fun addMixIn(target: Class<*>?, mixinSource: Class<*>?): Nothing = throw UnsupportedOperationException()

    override fun setMixIns(sourceMixins: Map<Class<*>?, Class<*>?>?): Nothing = throw UnsupportedOperationException()

    override fun getSerializerProvider(): Nothing = throw UnsupportedOperationException()

    override fun setSerializerProvider(p: DefaultSerializerProvider): Nothing = throw UnsupportedOperationException()

    override fun getSerializerFactory(): Nothing = throw UnsupportedOperationException()

    override fun setSerializerFactory(f: SerializerFactory): Nothing = throw UnsupportedOperationException()

    override fun getDeserializationContext(): Nothing = throw UnsupportedOperationException()

    override fun findAndRegisterModules(): Nothing = throw UnsupportedOperationException()

    override fun acceptJsonFormatVisitor(type: Class<*>?, visitor: JsonFormatVisitorWrapper): Nothing =
        throw UnsupportedOperationException()

    override fun acceptJsonFormatVisitor(type: JavaType, visitor: JsonFormatVisitorWrapper): Nothing =
        throw UnsupportedOperationException()

    @Suppress("DEPRECATION")
    override fun generateJsonSchema(t: Class<*>?): Nothing = throw UnsupportedOperationException()

    override fun copy(): ObjectMapper = delegate.copy()

    override fun version(): Version = delegate.version()

    override fun getSerializationConfig(): SerializationConfig = delegate.serializationConfig

    override fun getDeserializationConfig(): DeserializationConfig = delegate.deserializationConfig

    override fun getTypeFactory(): TypeFactory = delegate.typeFactory

    override fun isEnabled(f: MapperFeature): Boolean = delegate.isEnabled(f)

    override fun isEnabled(f: SerializationFeature): Boolean = delegate.isEnabled(f)

    override fun isEnabled(f: DeserializationFeature): Boolean = delegate.isEnabled(f)

    override fun isEnabled(f: JsonParser.Feature): Boolean = delegate.isEnabled(f)

    override fun isEnabled(f: JsonGenerator.Feature): Boolean = delegate.isEnabled(f)

    override fun isEnabled(f: JsonFactory.Feature): Boolean = delegate.isEnabled(f)

    @Throws(IOException::class)
    override fun <T> readValue(jp: JsonParser, valueType: Class<T>): T = delegate.readValue(jp, valueType)

    @Throws(IOException::class)
    override fun <T> readValue(jp: JsonParser, valueType: JavaType): T = delegate.readValue(jp, valueType)

    @Throws(IOException::class)
    override fun <T : TreeNode?> readTree(parser: JsonParser): T = delegate.readTree(parser)

    @Throws(IOException::class)
    override fun <T> readValues(parser: JsonParser, valueType: ResolvedType): MappingIterator<T> =
        delegate.readValues(parser, valueType)

    @Throws(IOException::class)
    override fun <T> readValues(jp: JsonParser, valueType: JavaType): MappingIterator<T> =
        delegate.readValues(jp, valueType)

    @Throws(IOException::class)
    override fun <T> readValues(jp: JsonParser, valueType: Class<T>): MappingIterator<T> =
        delegate.readValues(jp, valueType)

    @Throws(IOException::class)
    override fun readTree(inputStream: InputStream): JsonNode = delegate.readTree(inputStream)

    @Throws(IOException::class)
    override fun readTree(r: Reader): JsonNode = delegate.readTree(r)

    @Throws(JsonProcessingException::class)
    override fun readTree(content: String): JsonNode = delegate.readTree(content)

    @Throws(IOException::class)
    override fun readTree(content: ByteArray): JsonNode = delegate.readTree(content)

    @Throws(IOException::class)
    override fun readTree(file: File): JsonNode = delegate.readTree(file)

    @Throws(IOException::class)
    override fun readTree(source: URL): JsonNode = delegate.readTree(source)

    @Throws(IOException::class)
    override fun writeValue(g: JsonGenerator, value: Any) {
        delegate.writeValue(g, value)
    }

    @Throws(IOException::class)
    override fun writeTree(jgen: JsonGenerator, rootNode: TreeNode) {
        delegate.writeTree(jgen, rootNode)
    }

    @Throws(IOException::class)
    override fun writeTree(jgen: JsonGenerator, rootNode: JsonNode) {
        delegate.writeTree(jgen, rootNode)
    }

    override fun createObjectNode(): ObjectNode = delegate.createObjectNode()

    override fun createArrayNode(): ArrayNode = delegate.createArrayNode()

    override fun treeAsTokens(n: TreeNode): JsonParser = delegate.treeAsTokens(n)

    @Throws(JsonProcessingException::class)
    override fun <T> treeToValue(n: TreeNode, valueType: Class<T>): T = delegate.treeToValue(n, valueType)

    @Throws(IllegalArgumentException::class)
    override fun <T : JsonNode?> valueToTree(fromValue: Any): T = delegate.valueToTree(fromValue)

    override fun canSerialize(type: Class<*>?): Boolean =
        delegate.canSerialize(type)

    override fun canSerialize(type: Class<*>?, cause: AtomicReference<Throwable>): Boolean =
        delegate.canSerialize(type, cause)

    override fun canDeserialize(type: JavaType): Boolean =
        delegate.canDeserialize(type)

    override fun canDeserialize(type: JavaType, cause: AtomicReference<Throwable>): Boolean =
        delegate.canDeserialize(type, cause)

    @Throws(IOException::class)
    override fun <T> readValue(src: File, valueType: Class<T>): T =
        delegate.readValue(src, valueType)

    @Throws(IOException::class)
    override fun <T> readValue(src: File, valueTypeRef: TypeReference<T>): T =
        delegate.readValue(src, valueTypeRef)

    @Throws(IOException::class)
    override fun <T> readValue(src: File, valueType: JavaType): T =
        delegate.readValue(src, valueType)

    @Throws(IOException::class)
    override fun <T> readValue(src: URL, valueType: Class<T>): T =
        delegate.readValue(src, valueType)

    @Throws(IOException::class)
    override fun <T> readValue(src: URL, valueTypeRef: TypeReference<T>): T =
        delegate.readValue(src, valueTypeRef)

    @Throws(IOException::class)
    override fun <T> readValue(src: URL, valueType: JavaType): T =
        delegate.readValue(src, valueType)

    @Throws(JsonProcessingException::class)
    override fun <T> readValue(content: String, valueType: Class<T>): T =
        delegate.readValue(content, valueType)

    @Throws(JsonProcessingException::class)
    override fun <T> readValue(content: String, valueTypeRef: TypeReference<T>): T =
        delegate.readValue(content, valueTypeRef)

    @Throws(JsonProcessingException::class)
    override fun <T> readValue(content: String, valueType: JavaType): T =
        delegate.readValue(content, valueType)

    @Throws(IOException::class)
    override fun <T> readValue(src: Reader, valueType: Class<T>): T =
        delegate.readValue(src, valueType)

    @Throws(IOException::class)
    override fun <T> readValue(src: Reader, valueTypeRef: TypeReference<T>): T =
        delegate.readValue(src, valueTypeRef)

    @Throws(IOException::class)
    override fun <T> readValue(src: Reader, valueType: JavaType): T =
        delegate.readValue(src, valueType)

    @Throws(IOException::class)
    override fun <T> readValue(src: InputStream, valueType: Class<T>): T =
        delegate.readValue(src, valueType)

    @Throws(IOException::class)
    override fun <T> readValue(src: InputStream, valueTypeRef: TypeReference<T>): T =
        delegate.readValue(src, valueTypeRef)

    @Throws(IOException::class)
    override fun <T> readValue(src: InputStream, valueType: JavaType): T =
        delegate.readValue(src, valueType)

    @Throws(IOException::class)
    override fun <T> readValue(src: ByteArray, valueType: Class<T>): T =
        delegate.readValue(src, valueType)

    @Throws(IOException::class)
    override fun <T> readValue(src: ByteArray, offset: Int, len: Int, valueType: Class<T>): T =
        delegate.readValue(src, offset, len, valueType)

    @Throws(IOException::class)
    override fun <T> readValue(src: ByteArray, valueTypeRef: TypeReference<T>): T =
        delegate.readValue(src, valueTypeRef)

    @Throws(IOException::class)
    override fun <T> readValue(src: ByteArray, offset: Int, len: Int, valueTypeRef: TypeReference<T>): T =
        delegate.readValue(src, offset, len, valueTypeRef)

    @Throws(IOException::class)
    override fun <T> readValue(src: ByteArray, valueType: JavaType): T =
        delegate.readValue(src, valueType)

    @Throws(IOException::class)
    override fun <T> readValue(src: ByteArray, offset: Int, len: Int, valueType: JavaType): T =
        delegate.readValue(src, offset, len, valueType)

    @Throws(IOException::class)
    override fun writeValue(resultFile: File, value: Any) {
        delegate.writeValue(resultFile, value)
    }

    @Throws(IOException::class)
    override fun writeValue(out: OutputStream, value: Any) {
        delegate.writeValue(out, value)
    }

    @Throws(IOException::class)
    override fun writeValue(w: Writer, value: Any) {
        delegate.writeValue(w, value)
    }

    @Throws(JsonProcessingException::class)
    override fun writeValueAsString(value: Any): String = delegate.writeValueAsString(value)

    @Throws(JsonProcessingException::class)
    override fun writeValueAsBytes(value: Any): ByteArray = delegate.writeValueAsBytes(value)

    override fun writer(): ObjectWriter = delegate.writer()

    override fun writer(feature: SerializationFeature): ObjectWriter = delegate.writer(feature)

    override fun writer(first: SerializationFeature, vararg other: SerializationFeature): ObjectWriter =
        delegate.writer(first, *other)

    override fun writer(df: DateFormat): ObjectWriter = delegate.writer(df)

    override fun writerWithView(serializationView: Class<*>?): ObjectWriter = delegate.writerWithView(serializationView)

    override fun writerFor(rootType: Class<*>?): ObjectWriter = delegate.writerFor(rootType)

    override fun writerFor(rootType: TypeReference<*>?): ObjectWriter = delegate.writerFor(rootType)

    override fun writerFor(rootType: JavaType): ObjectWriter = delegate.writerFor(rootType)

    override fun writer(pp: PrettyPrinter): ObjectWriter = delegate.writer(pp)

    override fun writerWithDefaultPrettyPrinter(): ObjectWriter = delegate.writerWithDefaultPrettyPrinter()

    override fun writer(filterProvider: FilterProvider): ObjectWriter = delegate.writer(filterProvider)

    override fun writer(schema: FormatSchema): ObjectWriter = delegate.writer(schema)

    override fun writer(defaultBase64: Base64Variant): ObjectWriter = delegate.writer(defaultBase64)

    override fun writer(escapes: CharacterEscapes): ObjectWriter = delegate.writer(escapes)

    override fun writer(attrs: ContextAttributes): ObjectWriter = delegate.writer(attrs)

    @Suppress("DEPRECATION")
    override fun writerWithType(rootType: Class<*>?): ObjectWriter = delegate.writerWithType(rootType)

    @Suppress("DEPRECATION")
    override fun writerWithType(rootType: TypeReference<*>?): ObjectWriter = delegate.writerWithType(rootType)

    @Suppress("DEPRECATION")
    override fun writerWithType(rootType: JavaType): ObjectWriter = delegate.writerWithType(rootType)

    override fun reader(): ObjectReader = delegate.reader()

    override fun reader(feature: DeserializationFeature): ObjectReader = delegate.reader(feature)

    override fun reader(first: DeserializationFeature, vararg other: DeserializationFeature): ObjectReader =
        delegate.reader(first, *other)

    override fun readerForUpdating(valueToUpdate: Any): ObjectReader = delegate.readerForUpdating(valueToUpdate)

    override fun readerFor(type: JavaType): ObjectReader = delegate.readerFor(type)

    override fun readerFor(type: Class<*>?): ObjectReader = delegate.readerFor(type)

    override fun readerFor(type: TypeReference<*>?): ObjectReader = delegate.readerFor(type)

    override fun reader(f: JsonNodeFactory): ObjectReader = delegate.reader(f)

    override fun reader(schema: FormatSchema): ObjectReader = delegate.reader(schema)

    override fun reader(injectableValues: InjectableValues): ObjectReader = delegate.reader(injectableValues)

    override fun readerWithView(view: Class<*>?): ObjectReader = delegate.readerWithView(view)

    override fun reader(defaultBase64: Base64Variant): ObjectReader = delegate.reader(defaultBase64)

    override fun reader(attrs: ContextAttributes): ObjectReader = delegate.reader(attrs)

    @Suppress("DEPRECATION")
    override fun reader(type: JavaType): ObjectReader = delegate.reader(type)

    @Suppress("DEPRECATION")
    override fun reader(type: Class<*>?): ObjectReader = delegate.reader(type)

    @Suppress("DEPRECATION")
    override fun reader(type: TypeReference<*>?): ObjectReader = delegate.reader(type)

    @Throws(IllegalArgumentException::class)
    override fun <T> convertValue(fromValue: Any, toValueType: Class<T>): T =
        delegate.convertValue(fromValue, toValueType)

    @Throws(IllegalArgumentException::class)
    override fun <T> convertValue(fromValue: Any, toValueTypeRef: TypeReference<T>): T =
        delegate.convertValue(fromValue, toValueTypeRef)

    @Throws(IllegalArgumentException::class)
    override fun <T> convertValue(fromValue: Any, toValueType: JavaType): T =
        delegate.convertValue(fromValue, toValueType)

    override fun toString(): String = (delegate::class.simpleName + "(name='$name')")
}
