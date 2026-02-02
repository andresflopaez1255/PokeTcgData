package com.hefestsoft.poketcgdata.data.repositories

import com.hefestsoft.poketcgdata.core.CardsApi
import com.hefestsoft.poketcgdata.data.dtos.CardDetailDTO
import com.hefestsoft.poketcgdata.data.dtos.ResponseDTO
import javax.inject.Inject

class CardsRepository @Inject constructor(
    private val apiService: CardsApi
)    {

    suspend fun getCardByID (id: String): ResponseDTO<CardDetailDTO> {
        val response = apiService.getCardByID(id)
        return response


    }

}