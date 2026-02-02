package com.hefestsoft.poketcgdata.data.dtos

import com.google.gson.annotations.SerializedName

data class PricingDTO(
    val cardmarket: CardMarketDto?,
    val tcgplayer: TcgPlayerDto?,
    val priceCharting: PriceChartingDto?
)

data class PriceChartingDto(
    val previousPrice: Double?,
    val currentPrice: Double,
    val change: Double?,
    val currency: String,
    val source: String,
    val url: String,
    val scrapedAt: String
)


data class CardMarketDto(
    val updated: String,
    val unit: String,
    val idProduct: Int,
    val avg: Double,
    val low: Double,
    val trend: Double,
    val avg1: Double,
    val avg7: Double,
    val avg30: Double,

    @SerializedName("avg-holo")
    val avgHolo: Double,

    @SerializedName("low-holo")
    val lowHolo: Double,

    @SerializedName("trend-holo")
    val trendHolo: Double,

    @SerializedName("avg1-holo")
    val avg1Holo: Double,

    @SerializedName("avg7-holo")
    val avg7Holo: Double,

    @SerializedName("avg30-holo")
    val avg30Holo: Double
)


data class TcgPlayerDto(
    val updated: String,
    val unit: String,
    val holofoil: HolofoilDto,

    @SerializedName("reverse-holofoil")
    val reverseHolofoil: HolofoilDto
)

data class HolofoilDto(
    val low: Double?,
    val mid: Double?,
    val high: Double?,
    val market: Double?,
    val directLow: Double?
)
