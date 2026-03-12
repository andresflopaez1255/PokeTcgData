package com.hefestsoft.poketcgdata.domain

import android.util.Log
import com.hefestsoft.poketcgdata.data.dtos.CardResumeDto
import com.hefestsoft.poketcgdata.data.dtos.ResponseDTO
import com.hefestsoft.poketcgdata.data.repositories.SetsRepository
import javax.inject.Inject

class GetCardsSetListUseCase @Inject constructor(private val setsRepository: SetsRepository) {

    suspend operator  fun invoke(setId: String, page: Int = 1): ResponseDTO<List<CardResumeDto>> {
        val response = setsRepository.getSetCards( setId = setId, page = page, pageSize = 50)
        Log.d("GetCardsSetListUseCase", "Cards received: ${response.data.size}")
        return response

    }

}