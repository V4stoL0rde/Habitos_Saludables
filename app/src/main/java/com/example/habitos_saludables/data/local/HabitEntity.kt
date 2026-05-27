package com.example.habitos_saludables.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey val id: String,
    val name: String,
    val isCompleted: Boolean,
    val streakDays: Int
)

