@file:Suppress("NOTHING_TO_INLINE")

package tyrell.callisto.base.kotlin.properties

import mu.KLoggable
import mu.KLogger
import mu.NamedKLogging
import mu.toKLogger
import org.slf4j.LoggerFactory
import tyrell.callisto.base.definition.LibraryApi
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/**
 * [LoggingDelegates] is the class that provide utility methods
 * for defining delegated properties holding logger instance.
 *
 * @author Mikhail Gostev
 * @since 0.0.1
 */
public object LoggingDelegates {

    /**
     * Creates property delegate that will provide [KLogger] object for instance that owns given property.
     */
    @LibraryApi
    public inline fun instanceLogger(): ReadOnlyProperty<Any, KLogger> =
        LoggerPropertyDelegate()

    /**
     * Creates property delegate that will provide [KLogger] object for class [ktClass].
     */
    @LibraryApi
    public inline fun classLogger(ktClass: KClass<*>): ReadOnlyProperty<Any, KLogger> =
        LoggerPropertyDelegate(ktClass = ktClass)

    /**
     * Creates property delegate that will provide [KLogger] object of name from [name].
     */
    @LibraryApi
    public inline fun namedLogger(name: String): ReadOnlyProperty<Any, KLogger> =
        LoggerPropertyDelegate(name = name)
}

@PublishedApi
internal class LoggerPropertyDelegate(
    ktClass: KClass<*>? = null,
    instance: Any? = null,
    name: String? = null,
) : ReadOnlyProperty<Any, KLogger> {

    @Volatile
    lateinit var logger: KLogger

    init {
        val logger: KLogger? = when {
            (ktClass != null) -> ClassKLoggable.fromKClass(ktClass)
            (instance != null) -> ClassKLoggable.fromInstance(instance)
            (name != null) -> NamedKLogging(name)
            else -> null
        }?.logger

        if (logger != null) {
            this.logger = logger
        }
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): KLogger {
        if (this::logger.isInitialized) {
            return this.logger
        }

        synchronized(this) {
            if (this::logger.isInitialized) {
                return this.logger
            }

            this.logger = ClassKLoggable.fromInstance(thisRef).logger
            return this.logger
        }
    }

    private class ClassKLoggable private constructor(jvmClass: Class<*>) : KLoggable {

        override val logger: KLogger = LoggerFactory.getLogger(jvmClass).toKLogger()

        override fun logger(): KLogger = this.logger

        override fun logger(name: String): Nothing = throw NotImplementedError()

        companion object {
            @JvmStatic
            fun fromJvmClass(jvmClass: Class<*>): ClassKLoggable = ClassKLoggable(jvmClass)

            @JvmStatic
            fun fromKClass(ktClass: KClass<*>): ClassKLoggable = fromJvmClass(ktClass.java)

            @JvmStatic
            fun fromInstance(instance: Any): ClassKLoggable = fromKClass(instance::class)
        }
    }
}
