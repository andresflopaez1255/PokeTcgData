package com.hefestsoft.poketcgdata.presentation.viewsModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hefestsoft.poketcgdata.data.dtos.CardResumeDto
import com.hefestsoft.poketcgdata.domain.GetLastSetsCards
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetsViewModels @Inject constructor(private val getLastSetsCards: GetLastSetsCards) : ViewModel() {
    val setsCardsData: MutableLiveData<List<CardResumeDto>> = MutableLiveData()
    val loadingSetCards: MutableLiveData<Boolean> = MutableLiveData()


    fun getSetsCards() {
        loadingSetCards.postValue(true)
        viewModelScope.launch {
            val response = getLastSetsCards()
            if (response.status == "200")
                loadingSetCards.postValue(false)
            setsCardsData.postValue(response.data)
        }


    }



}