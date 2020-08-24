package com.weather.android

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.weather.android.logic.model.Place

class WeatherApplication : Application() {

    companion object{

        const val TOKEN = "8h4bJtU13IsAX0rC"

        lateinit var context: Context

        fun saveFirstBoot(){
            sharedPreferences().edit{
                putBoolean("firstBoot", true)
            }
        }

        fun getFirstBoot(): Boolean {
            return sharedPreferences().getBoolean("firstBoot",false)

        }

        fun isFirstBootSaved() = sharedPreferences().contains("firstBoot")

        fun sharedPreferences() = context.getSharedPreferences("firstBoot",Context.MODE_PRIVATE)
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }


}