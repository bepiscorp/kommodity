package tyrell.callisto.data.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asPublisher
import kotlinx.coroutines.reactive.awaitLast
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.runInterruptible
import org.reactivestreams.Publisher
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.core.io.buffer.DefaultDataBuffer
import org.springframework.core.io.buffer.DefaultDataBufferFactory
import reactor.core.publisher.Flux
import tyrell.callisto.base.definition.ExperimentalLibraryApi
import tyrell.callisto.base.vo.ByteArrayWrapper
import tyrell.callisto.base.vo.ByteArrayWrapper.Companion.asWrapper
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousFileChannel
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import kotlin.io.path.deleteIfExists
import kotlin.io.path.exists
import kotlin.io.path.isReadable
import kotlin.io.path.isWritable

private val WRITING_FACTORY = DefaultDataBufferFactory(true, DEFAULT_BUFFER_SIZE)

private val READING_FACTORY: DefaultDataBufferFactory = DefaultDataBufferFactory.sharedInstance

// 1 << 32 == 4GB
// 1 << 28 == 256MB
private const val READ_MEMORY_LIMIT: Int = 1.shr(28)

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@ExperimentalLibraryApi
public suspend fun Path.createAndWrite(bytesSequence: Flow<ByteBuffer>): Long {
    check(!exists() && parent.isWritable())

    openChannel(StandardOpenOption.WRITE, StandardOpenOption.CREATE).use { channel: AsynchronousFileChannel ->
        val bytesPublisher: Publisher<DataBuffer> = bytesSequence
            .map { inputBuffer -> WRITING_FACTORY.wrap(inputBuffer) }
            .asPublisher(currentCoroutineContext())

        return DataBufferUtils.write(bytesPublisher, channel)
            .reduce(0L) { current: Long, buffer: DataBuffer -> current + buffer.readPosition().toLong() }
            .doOnError { this@createAndWrite.deleteIfExists() }
            .awaitLast()
    }
}

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@ExperimentalLibraryApi
public suspend fun Path.readBytes(): ByteArrayWrapper {
    check(exists() && isReadable())

    val fileBuffers: Flux<DataBuffer> = DataBufferUtils.readAsynchronousFileChannel(
        { openChannelBlocking(StandardOpenOption.READ) },
        READING_FACTORY, DEFAULT_BUFFER_SIZE,
    )

    val resultBuffer: DataBuffer = DataBufferUtils.join(
        fileBuffers,
        READ_MEMORY_LIMIT,
    ).awaitSingle()

    val castedBuffer: DefaultDataBuffer = resultBuffer.let { it as DefaultDataBuffer }
    val resultByteBuffer: ByteBuffer = castedBuffer.nativeBuffer

    return resultByteBuffer.asWrapper()
}

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@ExperimentalLibraryApi
public suspend fun ByteArrayWrapper.write(path: Path) {
    val wrapper: ByteArrayWrapper = this

    check(wrapper.size > 0)
    check(!path.exists() && path.parent.exists() && path.parent.isWritable())

    path.openChannel(StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW).use { channel ->
        DataBufferUtils.write(Flux.just(WRITING_FACTORY.wrap(wrapper.unwrap())), channel)
    }.awaitLast()
}

private suspend fun Path.openChannel(vararg options: StandardOpenOption): AsynchronousFileChannel =
    runInterruptible(Dispatchers.IO) { openChannelBlocking(*options) }

private fun Path.openChannelBlocking(vararg options: StandardOpenOption): AsynchronousFileChannel =
    AsynchronousFileChannel.open(this, *options)
