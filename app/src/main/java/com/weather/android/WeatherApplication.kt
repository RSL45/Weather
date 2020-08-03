package com.weather.android

import android.app.Application
import android.content.Context

class WeatherApplication : Application() {

    companion object{

        const val TOKEN = "8h4bJtU13IsAX0rC"

        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}