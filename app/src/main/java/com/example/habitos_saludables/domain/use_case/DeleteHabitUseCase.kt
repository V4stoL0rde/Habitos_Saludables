package com.example.habitos_saludables.domain.use_case

import com.example.habitos_saludables.domain.model.Habit
import com.example.habitos_saludables.domain.repository.HabitRepository
import javax.inject.Inject

class DeleteHabitUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    suspend operator fun invoke(habit: Habit) = repository.deleteHabit(habit)
}