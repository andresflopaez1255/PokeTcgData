package com.hefestsoft.poketcgdata.data.dtos

data class CardSetDto(
    val id: String,
    val name: String,
    val symbol: String,
    val total: Int,
)

data class SetDTO (
    val id: String,
    val name: String,
    val logo: String?,
    val cardCount: CardCount
)