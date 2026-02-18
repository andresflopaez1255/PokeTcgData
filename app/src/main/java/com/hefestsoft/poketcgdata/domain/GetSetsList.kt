package com.hefestsoft.poketcgdata.domain

import com.hefestsoft.poketcgdata.data.dtos.ResponseDTO
import com.hefestsoft.poketcgdata.data.dtos.SetDTO
import com.hefestsoft.poketcgdata.data.repositories.SetsRepository
import javax.inject.Inject

class GetSetsList @Inject constructor(private val setsRepository: SetsRepository) {
    suspend operator fun invoke(): ResponseDTO<List<SetDTO>> {
        val response = setsRepository.getSets(page = 1, pageSize = 100)
        return response
    }
}

