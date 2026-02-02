package com.hefestsoft.poketcgdata.domain

import com.hefestsoft.poketcgdata.core.CardsApi
import com.hefestsoft.poketcgdata.data.dtos.CardResumeDto
import com.hefestsoft.poketcgdata.data.dtos.ResponseDTO
import com.hefestsoft.poketcgdata.data.repositories.SetsRepository
import javax.inject.Inject

class GetLastSetsCards @Inject constructor(private val setsRepository: SetsRepository) {
    suspend operator fun invoke(): ResponseDTO<List<CardResumeDto>> {
        val response = setsRepository.getSets()
        return response

    }
}