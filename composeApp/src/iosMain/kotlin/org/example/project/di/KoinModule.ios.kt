package org.example.project.di

import org.example.project.data.DatabaseDriverFactory
import org.example.project.data.IosDatabaseDriverFactory
import org.koin.dsl.module

actual val targetModule = module {
    single<DatabaseDriverFactory> { IosDatabaseDriverFactory() }
}
