package org.example.project

import android.app.Application
import org.example.project.di.initializeKoin
import org.koin.android.ext.koin.androidContext

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Firebase
        initializeKoin(
            config = { androidContext(this@MyApplication) }
        )
    }
}