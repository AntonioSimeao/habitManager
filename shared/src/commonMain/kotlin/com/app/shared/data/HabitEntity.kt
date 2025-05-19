package com.app.shared.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "habitos")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nome: String,
    val data: String,
    val concluido: Boolean
)
