package com.udacity.asteroidradar.util

import java.text.SimpleDateFormat
import java.util.*

class Util {
    companion object {
        fun getStartDateStr(): String {
            val cal = Calendar.getInstance()
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            //cal.add(Calendar.DAY_OF_YEAR, -1)
            return sdf.format(Date(cal.timeInMillis))
        }

        fun getEndDateStr(): String {
            val cal = Calendar.getInstance()
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            cal.add(Calendar.DAY_OF_YEAR, 7)
            return sdf.format(Date(cal.timeInMillis))
        }

        fun getStartDate(): Date {
            val cal = Calendar.getInstance()
            val s = SimpleDateFormat("yyyy-MM-dd")
            return s.parse(s.format(Date(cal.timeInMillis)))
        }

        fun getDate(dateStr:String): Date {
            val cal = Calendar.getInstance()
            val s = SimpleDateFormat("yyyy-MM-dd")
            return s.parse(dateStr)
        }

        fun getDateStr(date:Date): String {
            val cal = Calendar.getInstance()
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            //cal.add(Calendar.DAY_OF_YEAR, -1)
            return sdf.format(date)
        }
    }
}