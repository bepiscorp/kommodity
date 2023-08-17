package tyrell.callisto.data.typing.type

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.createType

public sealed class BasicDataType<out T : Any>(valueType: KType) : AbstractDataType<T>(valueType) {

    override val name: KString = this::class.simpleName!!

    private constructor(valueClass: KClass<*>) : this(valueClass.createType())

    public object Boolean : BasicDataType<KBoolean>(KBoolean::class)

    public object Double : BasicDataType<KDouble>(KDouble::class)

    public object Integer : BasicDataType<KInt>(KInt::class)

    public object Long : BasicDataType<KLong>(KLong::class)

    public object String : BasicDataType<KString>(KString::class)

    public object Date : BasicDataType<LocalDate>(LocalDate::class)

    public object Time : BasicDataType<LocalTime>(LocalTime::class)

    public object DateTime : BasicDataType<LocalDateTime>(LocalDateTime::class)

    public object Timestamp : BasicDataType<LocalDateTime>(JTimestamp::class)
}

private typealias KBoolean = Boolean
private typealias KDouble = Double
private typealias KInt = Int
private typealias KLong = Long
private typealias KString = String
private typealias JTimestamp = java.sql.Timestamp
