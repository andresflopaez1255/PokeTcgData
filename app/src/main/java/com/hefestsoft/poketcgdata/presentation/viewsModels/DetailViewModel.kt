package com.hefestsoft.poketcgdata.presentation.viewsModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hefestsoft.poketcgdata.data.dtos.CardDetailDTO
import com.hefestsoft.poketcgdata.domain.GetCardByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val getCardByIdUseCase: GetCardByIdUseCase) : ViewModel( ) {
    val detailCardData: MutableLiveData<CardDetailDTO> = MutableLiveData()
    val loadingDetailCard: MutableLiveData<Boolean> = MutableLiveData()

    fun getCardById(id: String) {
        loadingDetailCard.postValue(true)
        viewModelScope.launch {
            val response = getCardByIdUseCase(id)
            if (response.status == "200")
                loadingDetailCard.postValue(false)
            detailCardData.postValue(response.data)
        }


        }
    }



