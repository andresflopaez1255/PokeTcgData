package com.hefestsoft.poketcgdata.domain

import com.hefestsoft.poketcgdata.data.dtos.CardDetailDTO
import com.hefestsoft.poketcgdata.data.dtos.ResponseDTO
import com.hefestsoft.poketcgdata.data.repositories.CardsRepository
import javax.inject.Inject

class GetCardByIdUseCase @Inject constructor( private  val cardsRepository: CardsRepository) {
    suspend operator fun invoke(id: String):  ResponseDTO<CardDetailDTO> {
        val response = cardsRepository.getCardByID(id)
        return response

    }

}