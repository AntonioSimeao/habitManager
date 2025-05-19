package com.app.habitmanager.data

import android.content.Context
import androidx.room.Room

object HabitDatabaseProvider {
    @Volatile private var INSTANCE: HabitDatabase? = null

    fun getDatabase(context: Context): HabitDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                HabitDatabase::class.java,
                "habit_db"
            ).build()
            INSTANCE = instance
            instance
        }
    }
}
