package com.example.jura20.fragments.HomeFragment.Server

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DateUtils {
    fun formateDate(time: Long): String? {
        val serverDateFormat: DateFormat = SimpleDateFormat(
            "EEE, dd MMM yyyy HH:mm:ss z",
            Locale.ENGLISH
        )
        serverDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"))
        return serverDateFormat.format(time)
    }
}