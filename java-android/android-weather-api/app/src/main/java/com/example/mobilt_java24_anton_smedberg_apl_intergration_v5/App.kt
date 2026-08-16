package com.example.mobilt_java24_anton_smedberg_apl_intergration_v5

import android.app.Application
import com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.util.NotificationUtils
import com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.util.ServiceLocator

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        NotificationUtils.createChannels(this) // <—
        ServiceLocator.init(this)
    }
}