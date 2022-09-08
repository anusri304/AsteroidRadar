package com.udacity.asteroidradar.util

import java.util.Date
import java.text.SimpleDateFormat

class Util {
    companion object {
        fun getTodaysDate(): String {
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            return sdf.format(Date())
        }
    }
}