package com.weather.android.logic.dao

import androidx.room.*
import com.weather.android.logic.model.ChoosePlaceData
import com.weather.android.logic.model.WeatherResponse

@Dao
interface ChoosePlaceDao {

    @Transaction
    @Insert(entity = ChoosePlaceData::class)
    suspend fun insertPlace(choosePlaceData: ChoosePlaceData) : Long

    @Transaction
    @Query("SELECT * FROM chooseplacedata ORDER BY id asc")
    suspend fun queryAllPlace() : MutableList<ChoosePlaceData>

    @Transaction
    @Query("SELECT * FROM chooseplacedata WHERE name = (:name)")
    suspend fun queryChoosePlaceByName(name : String) : ChoosePlaceData?

    @Transaction
    @Query("UPDATE chooseplacedata SET skycon = (:skycon),description = (:description),temperature = (:temperature),max = (:max),min = (:min) WHERE name = (:name)")
    suspend fun updateChoosePlace(name : String,skycon:String,description : String,temperature : Int,max : Int,min : Int)

    @Transaction
    @Query("UPDATE chooseplacedata SET name = (:name),lng = (:lng),lat = (:lat),skycon = (:skycon),description = (:description),temperature = (:temperature),max = (:max),min = (:min) WHERE id = 1")
    suspend fun updateLocatePlace(name : String,lng:String,lat:String,skycon:String,description : String,temperature : Int,max : Int,min : Int)

    @Transaction
    @Delete(entity = ChoosePlaceData::class)
    suspend fun deleteChoosePlace(choosePlaceData: ChoosePlaceData) : Int

    @Transaction
    @Query("DELETE FROM chooseplacedata where name=:name")
    suspend fun deleteChoosePlaceByName(name: String) : Int
}