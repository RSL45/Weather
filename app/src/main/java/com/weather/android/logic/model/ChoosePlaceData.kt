package com.weather.android.logic.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ChoosePlaceData(
    var name:String,
    var lng:String,
    var lat:String,
    var skycon:String,
    var description: String,
    var temperature:Int,
    var max:Int,
    var min:Int
){
    @PrimaryKey(autoGenerate = true)
    var id:Long = 0
}