package com.example.habitos_saludables.presentation.habits_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitos_saludables.domain.model.Habit
import com.example.habitos_saludables.domain.repository.WeatherRepository
import com.example.habitos_saludables.domain.use_case.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class HabitViewModel @Inject constructor(
    private val getHabitsUseCase: GetHabitsUseCase,
    private val addHabitUseCase: AddHabitUseCase,
    private val deleteHabitUseCase: DeleteHabitUseCase,
    private val updateHabitUseCase: UpdateHabitUseCase,
    private val getDailyQuoteUseCase: GetDailyQuoteUseCase,
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HabitUiState())
    val uiState: StateFlow<HabitUiState> = _uiState.asStateFlow()

    init { loadContent() }

    private fun loadContent() {
        viewModelScope.launch {
            val quote = getDailyQuoteUseCase()
            val temp = weatherRepository.getCurrentTemperature()
            _uiState.update { it.copy(motivationalQuote = quote.text, motivationalAuthor = quote.author, temperature = temp) }

            getHabitsUseCase().collect { list -> _uiState.update { it.copy(habits = list) } }
        }
    }

    fun onAddHabit(name: String) {
        val cleanName = name.trim()
        // Verifica si ya existe ignorando mayúsculas/minúsculas
        val alreadyExists = _uiState.value.habits.any { it.name.equals(cleanName, ignoreCase = true) }

        if (cleanName.isNotEmpty() && !alreadyExists) {
            viewModelScope.launch {
                addHabitUseCase(Habit(UUID.randomUUID().toString(), cleanName, false, 0))
            }
        }
    }

    fun onDeleteHabit(habit: Habit) {
        viewModelScope.launch { deleteHabitUseCase(habit) }
    }

    fun onToggleHabit(habit: Habit) {
        viewModelScope.launch {
            val newStatus = !habit.isCompleted
            val newStreak = if (newStatus) habit.streakDays + 1 else if (habit.streakDays > 0) habit.streakDays - 1 else 0
            updateHabitUseCase(habit.copy(isCompleted = newStatus, streakDays = newStreak))
        }
    }

    fun onEditHabit(habit: Habit, newName: String) {
        val cleanName = newName.trim()
        // Verifica si OTRO hábito distinto ya tiene este nombre
        val alreadyExists = _uiState.value.habits.any {
            it.id != habit.id && it.name.equals(cleanName, ignoreCase = true)
        }

        if (cleanName.isNotEmpty() && !alreadyExists) {
            viewModelScope.launch { updateHabitUseCase(habit.copy(name = cleanName)) }
        }
    }
}