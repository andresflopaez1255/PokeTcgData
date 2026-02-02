package com.hefestsoft.poketcgdata.data.dtos

data class CardResumeDto(
    val id: String,
    val localID:String,
    val name: String,
    val image: String,
    val rarity: String,
    val set: CardSetDto,
    val category: String,
)
