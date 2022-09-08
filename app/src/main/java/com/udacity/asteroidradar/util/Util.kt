package com.udacity.asteroidradar.util

import java.text.SimpleDateFormat
import java.util.*

class Util {
    companion object {
        fun getStartDate(): String {
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            return sdf.format(Date())
        }

        fun getEndDate(): String {
            val cal = Calendar.getInstance()
            val s = SimpleDateFormat("yyyy-MM-dd")
            cal.add(Calendar.DAY_OF_YEAR, 7)
            return s.format(Date(cal.timeInMillis))
        }
    }
}