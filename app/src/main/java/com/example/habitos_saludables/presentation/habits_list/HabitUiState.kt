package com.example.habitos_saludables.presentation.habits_list

import com.example.habitos_saludables.domain.model.Habit

data class HabitUiState(
    val habits: List<Habit> = emptyList(),
    val motivationalQuote: String = "Cargando frase...",
    val motivationalAuthor: String = "",
    val temperature: String = "Cargando...",
    val isLoading: Boolean = false
)