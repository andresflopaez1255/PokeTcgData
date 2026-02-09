package com.hefestsoft.poketcgdata.domain

import com.hefestsoft.poketcgdata.data.dtos.PriceChartingDto
import com.hefestsoft.poketcgdata.data.repositories.CardsRepository
import javax.inject.Inject

class GetPriceChartingDataUseCase @Inject constructor(private val cardsRepository: CardsRepository) {

    suspend operator fun invoke(cardSlug: String, setSlug: String): PriceChartingDto {
        val response = cardsRepository.getPriceChartingData(cardSlug, setSlug)
        return response


    }
}