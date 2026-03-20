package com.hefestsoft.poketcgdata.core

import com.hefestsoft.poketcgdata.data.dtos.CardDetailDTO
import com.hefestsoft.poketcgdata.data.dtos.CardResumeDto
import com.hefestsoft.poketcgdata.data.dtos.MetadataResponseDto
import com.hefestsoft.poketcgdata.data.dtos.MetadataSetDto
import com.hefestsoft.poketcgdata.data.dtos.PriceChartingDto
import com.hefestsoft.poketcgdata.data.dtos.ResponseDTO
import com.hefestsoft.poketcgdata.data.dtos.SetDTO
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

    @GET("searchs/cards")
    suspend fun searchCards(
        @Query("name") name: String,
        @Query("type") type: String?,
        @Query("category") category: String?,
        @Query("set") set: String?,
        @Query("rarity") rarity: String?,
        @Query("page") page: Int? = 1,
        @Query("pageSize") pageSize: Int? = 100
    ): ResponseDTO<List<CardResumeDto>>

    @GET("metadata/rarities")
    suspend fun getRarities(): List<String>

    @GET("metadata/types")
    suspend fun getTypes(): List<String>

    @GET("metadata/card-types")
    suspend fun getCardTypes(): List<String>

    @GET("metadata/sets")
    suspend fun getMetadataSets(): MetadataResponseDto<List<MetadataSetDto>>

    @GET("searchs/sets")
    suspend fun searchSets(
        @Query("query") query: String,

    ): ResponseDTO<List<SetDTO>>

    @GET("sets/all")
    suspend fun getSets(
        @Query("page") page: Int = 1,
        @Query("pageSize") pageSize: Int = 100
    ): ResponseDTO<List<SetDTO>>

    @GET("cards/set/{setCode}")
    suspend fun getSetCards(
        @Path("setCode") setCode: String,
        @Query("page") page: Int = 1,
        @Query("pageSize") pageSize: Int = 100
    ): ResponseDTO<List<CardResumeDto>>
}
