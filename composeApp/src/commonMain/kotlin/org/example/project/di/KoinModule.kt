package org.example.project.di

import org.example.project.navigation.Navigator
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val koinModule = module {
    singleOf(::Navigator)
}

// initiate it separately in the Android main and ios main
fun initializeKoin(
    config: (KoinApplication. () -> Unit)? = null
) {
    startKoin {
        config?.invoke(this)
        modules(koinModule)
    }
}
