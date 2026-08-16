package com.example.mobilt_java24_anton_smedberg_lifecycle_v5

import android.app.Application
import android.util.Log
import com.example.mobilt_java24_anton_smedberg_lifecycle_v5.di.ServiceLocator

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.d("Lifecycle", "ServiceLocator START")
        ServiceLocator.init(this)
        Log.d("Lifecycle", "ServiceLocator END")
    }
}