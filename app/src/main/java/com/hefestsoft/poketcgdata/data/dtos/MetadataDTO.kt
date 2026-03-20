package com.hefestsoft.poketcgdata.data.dtos

data class MetadataResponseDto<T>(
    val status: Int,
    val message: String,
    val data: T
)

data class MetadataSetDto(
    val id: String,
    val name: String
)

data class SearchMetadataDto(
    val rarities: List<String>,
    val types: List<String>,
    val cardTypes: List<String>,
    val sets: List<MetadataSetDto>
)
