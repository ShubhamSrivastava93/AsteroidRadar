package com.udacity.asteroidradar.utils

import java.text.SimpleDateFormat
import java.util.*

class DateUtils {

    companion object {
        fun getCurrentDate(): String {
            val calendar = Calendar.getInstance()
            val currentTime = calendar.time
            val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
            return dateFormat.format(currentTime)
        }

        fun getEndDate(days: Int): String {
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
            calendar.add(Calendar.DAY_OF_YEAR, days)
            return dateFormat.format(calendar.time)
        }
    }
}