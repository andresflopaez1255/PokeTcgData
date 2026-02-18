package com.hefestsoft.poketcgdata.presentation.viewsModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hefestsoft.poketcgdata.data.dtos.CardDetailDTO
import com.hefestsoft.poketcgdata.data.dtos.PriceChartingDto
import com.hefestsoft.poketcgdata.domain.GetCardByIdUseCase
import com.hefestsoft.poketcgdata.domain.GetPriceChartingDataUseCase
import com.hefestsoft.poketcgdata.utils.normalizeLocalId
import com.hefestsoft.poketcgdata.utils.slugify
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val getCardByIdUseCase: GetCardByIdUseCase, private val getPriceChartingDataUseCase: GetPriceChartingDataUseCase) : ViewModel( ) {
    val detailCardData: MutableLiveData<CardDetailDTO> = MutableLiveData()

    val loadingDetailCard: MutableLiveData<Boolean> = MutableLiveData()

    val priceChartingData: MutableLiveData<PriceChartingDto> = MutableLiveData()


    fun getCardById(id: String) {
        loadingDetailCard.postValue(true)
        viewModelScope.launch {
            try {
                val response = getCardByIdUseCase(id)
                if (response.status == "200") {
                    detailCardData.postValue(response.data)
                }
            } catch (e: Exception) {
                Log.e("DetailViewModel", "Error fetching card details: ${e.message}")
            } finally {
                loadingDetailCard.postValue(false)
            }
        }


    }


    fun getPriceChartingData(cardSlug: String, setSlug: String) {
        loadingDetailCard.postValue(true)
        viewModelScope.launch {
            try {
                val response = getPriceChartingDataUseCase(cardSlug, setSlug)
                priceChartingData.postValue(response)
            } catch (e: Exception) {
                Log.e("DetailViewModel", "Error fetching price charting data: ${e.message}")
            } finally {
                loadingDetailCard.postValue(false)
            }
        }

    }


    }
