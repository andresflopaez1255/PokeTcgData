package com.hefestsoft.poketcgdata.core

import com.hefestsoft.poketcgdata.data.dtos.CardDetailDTO
import com.hefestsoft.poketcgdata.data.dtos.CardResumeDto
import com.hefestsoft.poketcgdata.data.dtos.PriceChartingDto
import com.hefestsoft.poketcgdata.data.dtos.ResponseDTO
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CardsApi {
    @GET("cards/last-set-cards")
    suspend fun getLastSetCards(
        @Query("page") page: Int = 1,
        @Query("pageSize") pageSize: Int = 20
    ): ResponseDTO<List<CardResumeDto>>

    @GET("cards/card/{id}")
    suspend fun getCardByID(
        @Path ("id") id: String
    ): ResponseDTO<CardDetailDTO>

    @GET("pricecharting/price")
    suspend fun getPriceChartingData(
        @Query("cardSlug") cardSlug: String,
        @Query("setSlug") setSlug: String
    ): PriceChartingDto

    @GET("cards/search")
    suspend fun searchCards(
        @Query("name") name: String,
        @Query("category") category: String?,
        @Query("set") set: String?,
        @Query("rarity") rarity: String?,
        @Query("page") page: Int? = 1,
        @Query("pageSize") pageSize: Int? = 20
    ): ResponseDTO<List<CardResumeDto>>
}
