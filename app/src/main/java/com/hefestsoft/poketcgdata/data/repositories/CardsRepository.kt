package com.hefestsoft.poketcgdata.data.repositories

import com.hefestsoft.poketcgdata.core.CardsApi
import com.hefestsoft.poketcgdata.data.dtos.CardDetailDTO
import com.hefestsoft.poketcgdata.data.dtos.CardQuery
import com.hefestsoft.poketcgdata.data.dtos.CardResumeDto
import com.hefestsoft.poketcgdata.data.dtos.PriceChartingDto
import com.hefestsoft.poketcgdata.data.dtos.ResponseDTO
import javax.inject.Inject

class CardsRepository @Inject constructor(
    private val apiService: CardsApi
)    {

    suspend fun getCardByID (id: String): ResponseDTO<CardDetailDTO> {
        val response = apiService.getCardByID(id)
        return response


    }

    suspend fun getPriceChartingData (cardSlug: String, setSlug: String): PriceChartingDto {
        val response = apiService.getPriceChartingData(cardSlug, setSlug)
        return response


    }


    suspend fun searchCards (
        cardQuery: CardQuery
    ): ResponseDTO<List<CardResumeDto>> {

        val response = apiService.searchCards(
            cardQuery.name,
            cardQuery.category,
            cardQuery.set,
            cardQuery.rarity,


        )
        return response


    }
}
