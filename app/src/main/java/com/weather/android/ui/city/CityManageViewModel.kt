package com.weather.android.ui.city

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weather.android.logic.ApiRepository
import com.weather.android.logic.ChoosePlaceRepository
import com.weather.android.logic.model.ChoosePlaceData
import com.weather.android.logic.model.Place
import kotlinx.coroutines.launch

class CityManageViewModel:ViewModel(){

    val choosePlaceLiveData = MutableLiveData<MutableList<ChoosePlaceData>>()

    fun queryAllChoosePlace(){
        viewModelScope.launch {
            choosePlaceLiveData.value = ChoosePlaceRepository.queryAllChoosePlace()
        }
    }

    fun deleteChoosePlaceByName(choosePlaceData: ChoosePlaceData){
        viewModelScope.launch {
            ChoosePlaceRepository.deleteChoosePlaceByName(choosePlaceData.name)
            Log.d("delete","deleted")
        }
    }
}