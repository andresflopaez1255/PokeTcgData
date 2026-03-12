package com.hefestsoft.poketcgdata.domain

import com.hefestsoft.poketcgdata.core.CardsApi
import com.hefestsoft.poketcgdata.data.dtos.ResponseDTO
import com.hefestsoft.poketcgdata.data.dtos.SetDTO
import javax.inject.Inject

class GetSearchSetUseCase @Inject constructor(private val cardsApi: CardsApi) {
    suspend operator fun invoke(query: String): ResponseDTO<List<SetDTO>> {
        val response = cardsApi.searchSets(query = query)
        return response

    }
}