package com.app.shared.data

import kotlinx.coroutines.flow.Flow

class HabitRepository(private val dao: HabitDao) {

    fun getHabitsByDate(date: String): Flow<List<HabitEntity>> {
        return dao.getHabitsByDate(date)
    }

    suspend fun insertHabit(habit: HabitEntity) {
        dao.insertHabit(habit)
    }

    suspend fun updateHabit(habit: HabitEntity) {
        dao.updateHabit(habit)
    }

    suspend fun deleteHabit(habit: HabitEntity) {
        dao.deleteHabit(habit)
    }
}
