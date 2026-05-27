package com.example.habitos_saludables.domain.use_case

import com.example.habitos_saludables.domain.model.Habit
import com.example.habitos_saludables.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHabitsUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    operator fun invoke(): Flow<List<Habit>> = repository.getHabits()
}