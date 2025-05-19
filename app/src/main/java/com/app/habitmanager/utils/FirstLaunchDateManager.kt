package com.app.habitmanager.util

import android.content.Context
import java.time.LocalDate

object FirstLaunchDateManager {
    private const val PREFS_NAME = "habit_prefs"
    private const val KEY_FIRST_LAUNCH_DATE = "first_launch_date"

    fun getFirstLaunchDate(context: Context): LocalDate {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val savedDate = prefs.getString(KEY_FIRST_LAUNCH_DATE, null)
        return if (savedDate != null) {
            LocalDate.parse(savedDate)
        } else {
            val today = LocalDate.now()
            prefs.edit().putString(KEY_FIRST_LAUNCH_DATE, today.toString()).apply()
            today
        }
    }
}
