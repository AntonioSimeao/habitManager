package com.app.habitmanager.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.shared.data.HabitDao
import com.app.shared.data.HabitEntity

@Database(entities = [HabitEntity::class], version = 1)
abstract class HabitDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
}
