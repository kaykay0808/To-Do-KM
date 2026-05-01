package org.example.project.di

import org.example.project.data.AndroidDatabaseDriverFactory
import org.example.project.data.DatabaseDriverFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val targetModule = module {
    single<DatabaseDriverFactory> { AndroidDatabaseDriverFactory(androidContext()) }
}


