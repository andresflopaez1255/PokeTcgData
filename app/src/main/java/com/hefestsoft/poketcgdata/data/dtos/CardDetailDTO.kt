package com.hefestsoft.poketcgdata.data.dtos

import com.google.gson.annotations.SerializedName


data class CardDetailDTO (
    val category: String,
    val id: String,
    val illustrator: String,
    val image: String,

    @SerializedName("localId")
    val localId: String,

    val name: String,
    val rarity: String,
    val set: Set,
    val  effect: String,
    val variants: Variants,
   val  trainerType: String?,

    @SerializedName("variants_detailed")
    val variantsDetailed: List<VariantsDetailed>,

    @SerializedName("dexId")
    val dexID: List<Long>?,

    val hp: Long?,
    val types: List<String>?,
    val evolveFrom: String?,
    val stage: String?,
    val abilities: List<Ability>?,
    val attacks: List<Attack>?,
    val retreat: Long?,
    val regulationMark: String,
    val legal: Legal,
    val updated: String,
    val pricing: Pricing
)

data class Ability (
    val type: String,
    val name: String,
    val effect: String
)

data class Attack (
    val cost: List<String>,
    val name: String,
    val effect: String,
    val damage: String
)

data class Legal (
    val standard: Boolean,
    val expanded: Boolean
)

data class Pricing (
    val cardmarket: CardMarketDto?,
    val tcgplayer: TcgPlayerDto?

)


data class Set (
    val cardCount: CardCount,
    val id: String,
    val logo: String,
    val name: String,
    val symbol: String
)

data class CardCount (
    val official: Long,
    val total: Long
)

data class Variants (
    val firstEdition: Boolean,
    val holo: Boolean,
    val normal: Boolean,
    val reverse: Boolean,
    val wPromo: Boolean
)

data class VariantsDetailed (
    val type: String,
    val size: String
)