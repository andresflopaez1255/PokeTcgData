package com.hefestsoft.poketcgdata.data.dtos

data class CardQuery (
    val name: String,
    val category: String?,
    val set: String? ,
    val rarity: String? ,
    val page: Int? = 1,
    val pageSize: Int? = 20
)
