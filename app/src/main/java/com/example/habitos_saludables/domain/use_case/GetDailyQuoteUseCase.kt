package com.example.habitos_saludables.domain.use_case

import com.example.habitos_saludables.domain.model.Quote
import com.example.habitos_saludables.domain.repository.QuoteRepository
import javax.inject.Inject

class GetDailyQuoteUseCase @Inject constructor(
    private val repository: QuoteRepository
) {
    suspend operator fun invoke(): Quote = repository.getDailyQuote()
}