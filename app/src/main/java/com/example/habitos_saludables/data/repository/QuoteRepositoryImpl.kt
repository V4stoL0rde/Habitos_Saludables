package com.example.habitos_saludables.data.repository

import com.example.habitos_saludables.domain.model.Quote
import com.example.habitos_saludables.domain.repository.QuoteRepository
import javax.inject.Inject

class QuoteRepositoryImpl @Inject constructor() : QuoteRepository {
    private val frasesEspanol = listOf(
        Quote("La disciplina es el puente entre metas y logros.", "Jim Rohn"),
        Quote("No cuentes los días, haz que los días cuenten.", "Muhammad Ali"),
        Quote("El éxito es la suma de pequeños esfuerzos diarios.", "Robert Collier"),
        Quote("Tu único límite es tu mente.", "Anónimo"),
        Quote("Haz hoy lo que otros no quieren, para mañana lograr lo que otros no pueden.", "Jerry Rice"),
        Quote("La motivación te pone en marcha, el hábito te mantiene.", "Jim Ryun"),
        Quote("Pequeños pasos en la dirección correcta pueden ser los más grandes de tu vida.", "Anónimo"),
        Quote("La salud no es algo que compras, es una inversión.", "Anónimo"),
        Quote("No te detengas hasta sentirte orgulloso.", "Anónimo"),
        Quote("Cuidar el cuerpo es una forma de amarse a uno mismo.", "Anónimo")
    )
    override suspend fun getDailyQuote() = frasesEspanol.random()
}