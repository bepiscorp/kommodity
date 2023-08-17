package tyrell.callisto.data.typing.serialization

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import tyrell.callisto.base.definition.LibraryApi
import java.io.IOException

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public class ByteArrayLogSerializer : JsonSerializer<ByteArray>() {

    @Throws(IOException::class)
    override fun serialize(value: ByteArray, jgen: JsonGenerator, provider: SerializerProvider) {
        jgen.writeString("ByteArray: Length=" + value.size)
    }
}
