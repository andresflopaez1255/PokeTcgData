package com.hefestsoft.poketcgdata.data.dtos

data class LanguagePreferenceRequestDto(
    val lang: String,
    val deviceId: String
)

data class LanguagePreferenceResponseDto(
    val lang: String
)
