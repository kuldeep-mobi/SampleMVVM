package com.mobikasa.samplemvvm

import android.app.Application

class MainApplication : Application() {
    companion object {
        lateinit var instance: MainApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        setupServices()
    }

    private fun setupServices() {
        WifiService.instance.initializeWithApplicationContext(this)
    }
}