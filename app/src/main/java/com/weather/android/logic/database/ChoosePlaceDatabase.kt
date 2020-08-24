package com.weather.android.logic.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.weather.android.logic.AnyConverters
import com.weather.android.logic.dao.ChoosePlaceDao
import com.weather.android.logic.model.ChoosePlaceData

@Database(version = 1,entities = [ChoosePlaceData::class])
@TypeConverters(AnyConverters::class)
abstract class ChoosePlaceDatabase :RoomDatabase(){

    abstract fun choosePlaceDao() : ChoosePlaceDao

    companion object{

        private var instance: ChoosePlaceDatabase? = null

        @Synchronized
        fun getDatabse(context: Context): ChoosePlaceDatabase{
            instance?.let {
                return it
            }
            synchronized(ChoosePlaceDatabase::class.java){
                return Room.databaseBuilder(context.applicationContext,ChoosePlaceDatabase::class.java,"choose_place_database")
                    .build().apply {
                        instance = this
                    }
            }
        }
    }
}