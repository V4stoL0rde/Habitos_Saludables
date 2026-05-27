package com.example.habitos_saludables.domain.model

data class Habit(
    val id: String,
    val name: String,
    val isCompleted: Boolean,
    val streakDays: Int
)