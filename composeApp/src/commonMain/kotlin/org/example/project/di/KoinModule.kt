package org.example.project.di

import org.example.project.data.FakeToDoRepository
import org.example.project.data.ToDoRepository
import org.example.project.navigation.Navigator
import org.example.project.presentation.screen.home.HomeViewModel
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
This code is doing two things:

(1) Defining a Koin module (a list of dependencies)

(2) Starting Koin manually in Android and iOS so shared code can use DI

This is very common in Kotlin Multiplatform.*/

val koinModule = module {
    singleOf(::Navigator)
    single<ToDoRepository> { FakeToDoRepository() }
    viewModelOf(::HomeViewModel)
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
