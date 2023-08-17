package tyrell.callisto.data.util

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.addDeserializer
import com.fasterxml.jackson.module.kotlin.addSerializer
import mu.KLogger
import tyrell.callisto.base.definition.DelicateLibraryApi
import tyrell.callisto.base.definition.LibraryApi
import tyrell.callisto.base.kotlin.properties.LoggingDelegates
import tyrell.callisto.base.messaging.model.DataRequest
import tyrell.callisto.data.serialization.buildImmutable
import tyrell.callisto.data.typing.DataRequestDto
import tyrell.callisto.data.typing.deserialization.DataTypeDeserializer
import tyrell.callisto.data.typing.serialization.ByteArrayLogSerializer
import tyrell.callisto.data.typing.serialization.DataTypeSerializer
import tyrell.callisto.data.typing.type.DataType
import java.util.concurrent.ConcurrentHashMap
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException
import javax.xml.xpath.XPath
import javax.xml.xpath.XPathFactory

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public object ThreadSafeObjects {

    private val logger: KLogger by LoggingDelegates.instanceLogger()

    public object JavaXml {

        private val documentBuilderFactory: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()

        private val xPathFactory: XPathFactory = XPathFactory.newDefaultInstance()

        private val xPathThreadLocal: ThreadLocal<XPath> = ThreadLocal.withInitial { xPathFactory.newXPath() }

        private val documentBuilderMap: MutableMap<Thread, DocumentBuilder> =
            ConcurrentHashMap<Thread, DocumentBuilder>()

        /**
         * Thread safe instance of [DocumentBuilder].
         * Nevertheless, the instance is cached, and it is safe to call this method multiple times in a row,
         * it is recommended to cache instance locally per one use
         * since [DocumentBuilder.reset] is called per each retrieval (in case the builder has been modified).
         *
         * This also means that:
         * - It is safe to fine-tune retrieved instance for one use;
         * - It is not recommended to cache instance reference locally between different uses,
         * since it can be reset in between.
         *
         * @return the preconfigured [DocumentBuilder]
         */
        @DelicateLibraryApi
        public val documentBuilder: DocumentBuilder get() = resolveDocumentBuilder()

        /**
         * Thread safe instance of [XPath].
         * Nevertheless, the instance is cached, and it is safe to call this method multiple times in a row,
         * it is recommended to cache instance locally per one use
         * since [XPath.reset] is called per each retrieval (in case the instance has been modified).
         *
         * This also means that:
         * - It is safe to fine-tune retrieved instance for one use;
         * - It is not recommended to cache instance reference locally between different uses,
         * since it can be reset in between.
         *
         * @return the preconfigured [XPath]
         */
        @DelicateLibraryApi
        public val xPath: XPath get() = resolveXPath()

        public fun <T> useDocumentBuilder(closure: (DocumentBuilder) -> T): T = closure.invoke(documentBuilder)

        public fun <T> useXPath(closure: (XPath) -> T): T = closure.invoke(xPath)

        private fun resolveXPath(): XPath = xPathThreadLocal.get().also { it.reset() }

        private fun resolveDocumentBuilder(): DocumentBuilder =
            documentBuilderMap.compute(Thread.currentThread()) { _, value ->
                if (value != null) {
                    value.reset()
                    value
                } else try {
                    documentBuilderFactory.newDocumentBuilder()
                } catch (ex: ParserConfigurationException) {
                    throw RuntimeException(
                        "Failed to instantiate DocumentBuilder." +
                            " It is likely caused by subsequent problems: ${ex.message}",
                        ex,
                    ).let(logger::throwing)
                }
            }!!
    }

    public object JsonMappers {

        public val plainMapper: ObjectMapper

        public val hibernateMapper: ObjectMapper

        /**
         * JSON object mappers initialization.
         */
        init {
            // Initializing custom modules
            val basicModule: SimpleModule = buildBaseModule()
            val hibernateModule: Hibernate5Module? = buildHibernateModule()
            val javaTimeModule: JavaTimeModule = buildJavaTimeModule()
            val kotlinModule: KotlinModule = buildKotlinModule()

            // Initializing mappers
            this.plainMapper = JsonMapper.builder().apply { // Plain mapper
                addModules(listOfNotNull(basicModule, javaTimeModule, kotlinModule))

                configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true)
                configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE, true)
            }.buildImmutable("JSON.plainMapper")

            this.hibernateMapper = JsonMapper.builder().apply { // Hibernate mapper
                addModules(listOfNotNull(basicModule, javaTimeModule, kotlinModule, hibernateModule))

                configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true)
                configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE, true)
            }.buildImmutable("JSON.hibernateMapper")
        }
    }

    public object XmlMappers {

        public val plainMapper: ObjectMapper

        public val hibernateMapper: ObjectMapper

        /**
         * XML object mappers initialization.
         */
        init {
            // Initializing custom modules
            val basicModule: SimpleModule = buildBaseModule()
            val hibernateModule: Hibernate5Module? = buildHibernateModule()
            val javaTimeModule: JavaTimeModule = buildJavaTimeModule()
            val kotlinModule: KotlinModule = buildKotlinModule()
            val xmlModule: JacksonXmlModule = buildXmlModule()

            // Initializing mappers
            this.plainMapper = XmlMapper.builder().apply { // Plain mapper
                addModules(listOfNotNull(basicModule, javaTimeModule, kotlinModule, xmlModule))
                enable(SerializationFeature.INDENT_OUTPUT)

                configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true)
                configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE, true)
            }.buildImmutable("XML.plainMapper")

            this.hibernateMapper = XmlMapper.builder().apply { // Hibernate mapper
                addModules(listOfNotNull(basicModule, javaTimeModule, kotlinModule, hibernateModule))
                enable(SerializationFeature.INDENT_OUTPUT)

                configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true)
                configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE, true)
            }.buildImmutable("XML.hibernateMapper")
        }
    }

    private fun buildBaseModule(): SimpleModule = SimpleModule().apply {
        addAbstractTypeMapping(Map::class.java, LinkedHashMap::class.java)
        addAbstractTypeMapping(MutableMap::class.java, LinkedHashMap::class.java)

        addAbstractTypeMapping(DataRequest::class.java, DataRequestDto::class.java)

        addSerializer(ByteArray::class.java, ByteArrayLogSerializer())

        addDeserializer(DataType::class, DataTypeDeserializer())
        addSerializer(DataType::class, DataTypeSerializer())
    }

    private fun buildHibernateModule(): Hibernate5Module? {
        if (!DatabaseUtils.isHibernateInClassPath()) {
            logger.warn { "Could not initialize [Hibernate5Module] as Hibernate is not in classpath" }
            return null
        }

        return Hibernate5Module().apply {
            disable(Hibernate5Module.Feature.USE_TRANSIENT_ANNOTATION)
        }
    }

    private fun buildJavaTimeModule(): JavaTimeModule = JavaTimeModule()

    private fun buildKotlinModule(): KotlinModule = KotlinModule.Builder()
        // Note: According to documentation this prop has significant performance impact
        .configure(KotlinFeature.StrictNullChecks, true)
        .build()

    private fun buildXmlModule(): JacksonXmlModule = JacksonXmlModule().apply {
        setDefaultUseWrapper(true)
    }
}
