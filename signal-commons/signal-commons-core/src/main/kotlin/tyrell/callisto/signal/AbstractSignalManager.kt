package tyrell.callisto.signal

import mu.KLogger
import org.springframework.beans.factory.BeanNameAware
import tyrell.callisto.base.definition.DelicateLibraryApi
import tyrell.callisto.base.definition.LibraryApi
import tyrell.callisto.base.kotlin.dsl.uncheckedCast
import tyrell.callisto.base.kotlin.properties.LoggingDelegates
import tyrell.callisto.signal.model.DomainSignal
import tyrell.callisto.signal.model.Signal
import tyrell.callisto.signal.model.SignalDefinition
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * [AbstractSignalManager] is a class that is responsible for signal creation
 *
 * @param DS domain signal
 *
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi @DelicateLibraryApi
public abstract class AbstractSignalManager<in SD : SignalDefinition, S : Signal, DS : DomainSignal> :
    SignalManager<SD, S, DS>, BeanNameAware {

    protected open val logger: KLogger by LoggingDelegates.instanceLogger()

    private lateinit var beanName: String

    override lateinit var signalType: String

    override val signalClass: Class<S>

    override val domainSignalClass: Class<DS>

    init {
        val genericSuperclass: ParameterizedType = (javaClass.genericSuperclass as ParameterizedType)

        this.signalClass = when (val type: Type = genericSuperclass.actualTypeArguments[1]) {
            is Class<*> -> type
            is ParameterizedType -> type.rawType
            else -> throw IllegalArgumentException(
                "Unable to get generic parameter type for [${type.typeName}]",
            ).let(logger::throwing)
        }.let(::uncheckedCast)

        this.domainSignalClass = when (val type: Type = genericSuperclass.actualTypeArguments[2]) {
            is Class<*> -> type
            is ParameterizedType -> type.rawType
            else -> throw IllegalArgumentException(
                "Unable to get generic parameter type for [${type.typeName}]",
            ).let(logger::throwing)
        }.let(::uncheckedCast)
    }

    override fun setBeanName(name: String) {
        this.beanName = name
        this.signalType = resolveSignalTypeNameByBeanName(name)
    }

    private fun resolveSignalTypeNameByBeanName(beanName: String): String {
        val components: List<String> = beanName.split('.')
        val signalTypeName: String = components.last()
        return signalTypeName
    }

    override fun toString(): String = "SignalManager(signalType='$signalType')"
}
