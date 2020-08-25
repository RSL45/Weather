package com.weather.android.ui.weather

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.weather.android.logic.model.ChoosePlaceResponse


class WeatherFragmentAdapter(choosePlaceList: MutableList<ChoosePlaceResponse>,fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    private val fragments: MutableList<Fragment> = mutableListOf()

    init {
        for (choosePlace in choosePlaceList){
            fragments.add(WeatherFragment.newInstance(choosePlace.id,choosePlace.name,choosePlace.lng,choosePlace.lat))
        }
    }

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}