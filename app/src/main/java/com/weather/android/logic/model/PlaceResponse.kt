package com.weather.android.logic.model

import com.google.gson.annotations.SerializedName

/*地址查询*/
data class PlaceResponse(val status:String,val places:List<Place>)

data class Place(val id:String,val name:String,val location:Location,@SerializedName("formatted_address") val address:String)

data class Location(val lng:String,val lat:String)