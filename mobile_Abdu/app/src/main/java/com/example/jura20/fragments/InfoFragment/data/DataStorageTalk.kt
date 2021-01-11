package com.example.jura20.fragments.InfoFragment.data

import com.example.jura20.R

class DataStorageTalk {
    fun list(): List<Talk>{
        return listOf(
            Talk(
                R.drawable.weather,
                "Weather"
            ),
            Talk(
                R.drawable.hobby,
                "Hobby"
            )
        )
    }
}