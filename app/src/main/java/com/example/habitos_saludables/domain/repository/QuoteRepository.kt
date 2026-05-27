package com.example.habitos_saludables.domain.repository

import com.example.habitos_saludables.domain.model.Quote

interface QuoteRepository {
    suspend fun getDailyQuote(): Quote
}