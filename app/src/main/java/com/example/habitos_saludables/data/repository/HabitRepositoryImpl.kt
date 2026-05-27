package com.example.habitos_saludables.data.repository

import com.example.habitos_saludables.data.local.HabitDao
import com.example.habitos_saludables.data.local.HabitEntity
import com.example.habitos_saludables.domain.model.Habit
import com.example.habitos_saludables.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HabitRepositoryImpl @Inject constructor(
    private val habitDao: HabitDao
) : HabitRepository {

    override fun getHabits(): Flow<List<Habit>> {
        return habitDao.getAllHabits().map { entities ->
            entities.map { Habit(it.id, it.name, it.isCompleted, it.streakDays) }
        }
    }

    override suspend fun addHabit(habit: Habit) {
        habitDao.insertHabit(habit.toEntity())
    }

    override suspend fun updateHabit(habit: Habit) {
        habitDao.updateHabit(habit.toEntity())
    }

    override suspend fun deleteHabit(habit: Habit) {
        habitDao.deleteHabit(habit.toEntity())
    }

    // Función para no repetir código
    private fun Habit.toEntity() = HabitEntity(id, name, isCompleted, streakDays)
}