package com.weather.android.logic

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.weather.android.logic.model.ChoosePlaceData
import com.weather.android.logic.model.WeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.lang.RuntimeException
import kotlin.coroutines.CoroutineContext

object ChoosePlaceRepository {

    suspend fun queryAllChoosePlace() = withContext(Dispatchers.IO){
        RoomHelper.queryAllChoosePlace()
    }

    fun queryFirstChoosePlace() = fire(Dispatchers.IO){
        val response = RoomHelper.queryFirstChoosePlace()
        Result.success(response)
    }

    suspend fun insertChoosePlace(choosePlaceData: ChoosePlaceData): Long? = withContext(Dispatchers.IO){
        RoomHelper.insertChoosePlace(choosePlaceData)
    }

    fun updateChoosePlace(name : String,skycon:String,description : String,temperature : Int,max : Int,min : Int) = fire(Dispatchers.IO){
        Result.success(RoomHelper.updateChoosePlace(name,skycon,description,temperature,max,min))
    }

    fun updateLocatePlace(name : String,lng:String,lat:String,skycon:String,description : String,temperature : Int,max : Int,min : Int) = fire(Dispatchers.IO){
        Result.success(RoomHelper.updateLocatePlace(name,lng,lat,skycon,description,temperature,max,min))
    }

    fun deleteChoosePlace(choosePlaceData: ChoosePlaceData) = fire(Dispatchers.IO){
        Result.success(RoomHelper.deleteChoosePlace(choosePlaceData))
    }

    suspend fun deleteChoosePlaceByName(name:String) = withContext(Dispatchers.IO){
        RoomHelper.deleteChoosePlaceByName(name)
    }

    private fun <T> fire(context: CoroutineContext, block:suspend ()->Result<T>) = liveData<Result<T>>(context) {
        val result = try {
            block()
        }catch (e: Exception){
            Result.failure<T>(e)
        }
        emit(result)
    }
}