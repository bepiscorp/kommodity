package tyrell.callisto.gradle.internal.util

import com.github.benmanes.caffeine.cache.AsyncLoadingCache
import com.github.benmanes.caffeine.cache.Caffeine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.future.future
import java.util.concurrent.Executor

inline fun <K : Any, V : Any> Caffeine<Any, Any>.suspendingLoadingCache(
    crossinline suspendedLoader: suspend (key: K) -> V,
): AsyncLoadingCache<K, V> = buildAsync { key, executor: Executor ->
    val scope = CoroutineScope(executor.asCoroutineDispatcher())
    scope.future { suspendedLoader(key) }
}
