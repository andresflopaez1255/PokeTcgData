package com.hefestsoft.poketcgdata.data.dtos

data class ResponseDTO<T>(val status: String, val message:String,  val data: T)