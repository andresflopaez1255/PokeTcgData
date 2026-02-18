package com.hefestsoft.poketcgdata.presentation.viewsModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hefestsoft.poketcgdata.data.dtos.CardResumeDto
import com.hefestsoft.poketcgdata.data.dtos.SetDTO
import com.hefestsoft.poketcgdata.domain.GetLastSetsCards
import com.hefestsoft.poketcgdata.domain.GetSetsList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetsViewModels @Inject constructor(private val getLastSetsCards: GetLastSetsCards,
                                         private val getSetsList: GetSetsList) : ViewModel() {
    val setsCardsData: MutableLiveData<List<CardResumeDto>> = MutableLiveData()
    val setsListData: MutableLiveData<List<SetDTO>> = MutableLiveData()
    val loadingSetCards: MutableLiveData<Boolean> = MutableLiveData()
    val loadingSetList: MutableLiveData<Boolean> = MutableLiveData()

    init {
        getLastSetsCardsList()
        getSets()


    }


    fun getLastSetsCardsList() {
        loadingSetCards.postValue(true)
        viewModelScope.launch {
            try {
                val response = getLastSetsCards()
                if (response.status == "200")
                    loadingSetCards.postValue(false)
                setsCardsData.postValue(response.data)
            } catch (e: Exception) {
                Log.e("SetsViewModels", "Error fetching last sets cards: ${e.message}")
                loadingSetCards.postValue(false)
            }
        }


    }

    fun getSets() {
        loadingSetList.postValue(true)
        viewModelScope.launch {
            try {
                val response = getSetsList()
                if (response.status == "200")
                    loadingSetList.postValue(false)
                setsListData.postValue(response.data)
            } catch (e: Exception) {
                Log.e("SetsViewModels", "Error fetching sets: ${e.message}")
                loadingSetList.postValue(false)
            }

        }


    }



}