package com.weather.android.ui.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weather.android.logic.ApiRepository
import com.weather.android.logic.ChoosePlaceRepository
import com.weather.android.logic.model.ChoosePlaceData
import com.weather.android.logic.model.Location
import com.weather.android.logic.model.WeatherResponse
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    var locationLng = ""
    var locationLat = ""
    var placeName = ""

    val choosePlaceLiveData = MutableLiveData<MutableList<ChoosePlaceData>>()

    fun queryAllChoosePlace(){
        viewModelScope.launch {
            choosePlaceLiveData.value = ChoosePlaceRepository.queryAllChoosePlace()
        }
    }

}