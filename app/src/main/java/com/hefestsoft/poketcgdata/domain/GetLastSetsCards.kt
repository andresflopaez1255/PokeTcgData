package com.hefestsoft.poketcgdata.domain

import android.util.Log
import com.hefestsoft.poketcgdata.data.dtos.CardResumeDto
import com.hefestsoft.poketcgdata.data.dtos.ResponseDTO
import com.hefestsoft.poketcgdata.data.repositories.SetsRepository
import javax.inject.Inject

class GetLastSetsCardsUseCase @Inject constructor(private val setsRepository: SetsRepository) {
    suspend operator fun invoke(page: Int = 1): ResponseDTO<List<CardResumeDto>> {
        val response = setsRepository.getLastSetsCard(page = page, pageSize = 50)
        Log.d("GetLastSetsCardsUseCase", "Cards received for page $page: ${response.data.size}")

        return response
    }
}
