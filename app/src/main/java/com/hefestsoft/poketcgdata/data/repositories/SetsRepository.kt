package com.hefestsoft.poketcgdata.data.repositories

import android.util.Log
import com.hefestsoft.poketcgdata.core.CardsApi
import com.hefestsoft.poketcgdata.data.dtos.CardResumeDto
import com.hefestsoft.poketcgdata.data.dtos.ResponseDTO
import com.hefestsoft.poketcgdata.data.dtos.SetDTO
import javax.inject.Inject

class SetsRepository @Inject constructor(private val cardsApi: CardsApi) {
    suspend fun getLastSetsCard(page: Int, pageSize: Int): ResponseDTO<List<CardResumeDto>> {
        val response = cardsApi.getLastSetCards(page = page, pageSize = pageSize)
        return response
    }

    suspend fun getSets(page: Int, pageSize: Int): ResponseDTO<List<SetDTO>> {
        val response = cardsApi.getSets(page = page, pageSize = pageSize)
        return response
    }

    suspend fun getSetCards(setId: String, page: Int, pageSize: Int): ResponseDTO<List<CardResumeDto>> {
        Log.d("SetsRepository", "Fetching set cards for set ID: $setId")
        val response = cardsApi.getSetCards(setCode = setId, page = page, pageSize = pageSize)
        return response
    }

    suspend fun searchSets(query: String): ResponseDTO<List<SetDTO>> {
        val response = cardsApi.searchSets(query = query)
        return response
    }
}
