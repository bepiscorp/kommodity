package tyrell.callisto.gradle.internal.repository

import com.github.benmanes.caffeine.cache.AsyncLoadingCache
import com.github.benmanes.caffeine.cache.Caffeine
import kotlinx.coroutines.future.await
import kotlinx.coroutines.supervisorScope
import tyrell.callisto.gradle.internal.exception.RecoverableRepositoryException
import tyrell.callisto.gradle.internal.repository.SpaceIdentifier.LOCAL_JUPITER_MOONS
import tyrell.callisto.gradle.internal.repository.SpaceIdentifier.LOCAL_STAR_NAMES
import tyrell.callisto.gradle.internal.repository.SpaceIdentifier.REMOTE_JUPITER_MOONS
import tyrell.callisto.gradle.internal.repository.SpaceIdentifier.REMOTE_STAR_NAMES
import tyrell.callisto.gradle.internal.util.JupiterMoonsScrapper
import tyrell.callisto.gradle.internal.util.StarNamesScrapper
import tyrell.callisto.gradle.internal.util.suspendingLoadingCache
import java.net.ConnectException
import java.net.UnknownHostException

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
object SpaceIdentifiersRepository {

    private val cache: AsyncLoadingCache<SpaceIdentifier, LookupResult> =
        Caffeine.newBuilder().suspendingLoadingCache(::loadListBy)

    suspend fun loadOrGetJupiterMoons(): Set<String> =
        loadOrGetIdentifiers(
            localIdentifier = LOCAL_JUPITER_MOONS,
            remoteIdentifier = REMOTE_JUPITER_MOONS
        ).toSet()

    suspend fun loadOrGetStarNames(): Set<String> =
        loadOrGetIdentifiers(
            localIdentifier = LOCAL_STAR_NAMES,
            remoteIdentifier = REMOTE_STAR_NAMES
        ).toSet()

    private suspend fun loadOrGetIdentifiers(
        localIdentifier: SpaceIdentifier,
        remoteIdentifier: SpaceIdentifier,
    ): List<String> = supervisorScope cl@{
        val localIdentifiersFuture = cache[localIdentifier]
        val remoteIdentifiersFuture = cache[remoteIdentifier]

        val localIdentifiers: LookupResult = localIdentifiersFuture.await()
        if (localIdentifiers.success) {
            return@cl localIdentifiers.getData()
        }

        val remoteIdentifiers: LookupResult = remoteIdentifiersFuture.await()
        if (remoteIdentifiers.success) {
            return@cl remoteIdentifiers.getData()
        }

        assert(!localIdentifiers.success && !remoteIdentifiers.success)
        throw when (val throwable = remoteIdentifiers.throwable!!) {
            is UnknownHostException, is ConnectException -> RecoverableRepositoryException(
                message = "Could not resolve remote host. Check your internet connection",
                cause = throwable
            )
            else -> throwable
        }
    }

    private suspend fun loadListBy(spaceIdentifier: SpaceIdentifier): LookupResult = runCatching {
        when (spaceIdentifier) {
            REMOTE_JUPITER_MOONS -> JupiterMoonsScrapper.fetchJupiterMoonNames()
            LOCAL_JUPITER_MOONS -> JupiterMoonsRepository.getJupiterMoons()
            REMOTE_STAR_NAMES -> StarNamesScrapper.fetchStarNames()
            LOCAL_STAR_NAMES -> throw NotImplementedError("Functionality not implemented") // TODO: Implement
        }
    }.fold(
        onSuccess = LookupResult::success,
        onFailure = LookupResult::failure
    )
}

private class LookupResult private constructor(
    private val data: List<String>,
    val throwable: Throwable?,
) {

    val success: Boolean = (throwable == null)

    fun getData(): List<String> = if (throwable == null) data else throw throwable

    companion object {

        @JvmStatic
        fun success(data: List<String>) = LookupResult(data = data, throwable = null)

        @JvmStatic
        fun failure(throwable: Throwable) = LookupResult(data = emptyList(), throwable = throwable)
    }
}

private enum class SpaceIdentifier {
    REMOTE_JUPITER_MOONS, LOCAL_JUPITER_MOONS,
    REMOTE_STAR_NAMES, LOCAL_STAR_NAMES,
}
