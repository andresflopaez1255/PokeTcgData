package com.hefestsoft.poketcgdata.data.repositories

import com.hefestsoft.poketcgdata.core.CardsApi
import com.hefestsoft.poketcgdata.data.dtos.CardResumeDto
import com.hefestsoft.poketcgdata.data.dtos.ResponseDTO
import com.hefestsoft.poketcgdata.data.dtos.SetDTO
import javax.inject.Inject

class SetsRepository @Inject constructor(private val cardsApi: CardsApi) {
    suspend fun getLastSetsCard(): ResponseDTO<List<CardResumeDto>> {
        val response = cardsApi.getLastSetCards(page = 1, pageSize = 50)
        return response
    }

    suspend fun getSets(page: Int, pageSize: Int): ResponseDTO<List<SetDTO>> {
        val response = cardsApi.getSets(page = page, pageSize = pageSize)
        return response
    }
}
