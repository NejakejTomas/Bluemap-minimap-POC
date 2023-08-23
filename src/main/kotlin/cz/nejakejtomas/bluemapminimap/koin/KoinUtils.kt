package cz.nejakejtomas.bluemapminimap.koin

import cz.nejakejtomas.bluemapminimap.LoopDispatcher
import cz.nejakejtomas.bluemapminimap.Mod.getKoin
import kotlinx.coroutines.CoroutineScope
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun createSingleLoopScope(instance: LoopDispatcher, name: String): Module {
    return module {
        single<CoroutineScope>(named(name)) { CoroutineScope(instance) }
    }
}

@OptIn(KoinInternalApi::class)
fun <T> injectAll(clazz: Class<*>): Lazy<List<T>> {
    return lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        val kClass = clazz.kotlin

        getKoin().scopeRegistry.rootScope.getAll(kClass)
    }
}

inline fun <reified T> injectAll(): Lazy<List<T>> = injectAll(T::class.java)