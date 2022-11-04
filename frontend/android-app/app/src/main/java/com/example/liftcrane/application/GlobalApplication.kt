package com.example.liftcrane.application

import android.app.Application
import android.content.Context
import android.util.Log

class GlobalApplication: Application() {

    companion object{
        @JvmStatic private lateinit var appContext: Context

        @JvmStatic public fun getAppContext(): Context{
            return appContext
        }
    }
    override fun onCreate() {
        Log.i("MyInfo", "app created")
        super.onCreate()
        appContext = applicationContext
    }
}