package com.hefestsoft.poketcgdata.presentation.viewsModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hefestsoft.poketcgdata.data.dtos.CardResumeDto
import com.hefestsoft.poketcgdata.data.dtos.SetDTO
import com.hefestsoft.poketcgdata.domain.GetCardsSetListUseCase
import com.hefestsoft.poketcgdata.domain.GetLastSetsCardsUseCase
import com.hefestsoft.poketcgdata.domain.GetSearchSetUseCase
import com.hefestsoft.poketcgdata.domain.GetSetsListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetsViewModels @Inject constructor(
    private val getLastSetsCards: GetLastSetsCardsUseCase,
    private val getSetsList: GetSetsListUseCase,
    private val getCardsSetList: GetCardsSetListUseCase,
    private val getSearchSetUseCase: GetSearchSetUseCase
) : ViewModel() {

    // Variables para Home (Last Sets)
    val setsCardsData: MutableLiveData<List<CardResumeDto>> = MutableLiveData(emptyList())
    val loadingSetCards: MutableLiveData<Boolean> = MutableLiveData()
    val loadingMoreCards: MutableLiveData<Boolean> = MutableLiveData()
    private var currentLastSetsPage = 1
    private var isLastPageLastSets = false

    // Variables para Sets List
    val setsListData: MutableLiveData<List<SetDTO>> = MutableLiveData()
    val loadingSetList: MutableLiveData<Boolean> = MutableLiveData()

    // Variables para Cards by Set (Detail List)
    val cardsSetData: MutableLiveData<List<CardResumeDto>> = MutableLiveData(emptyList())
    val loadingCardsSet: MutableLiveData<Boolean> = MutableLiveData()
    val loadingMoreCardsSet: MutableLiveData<Boolean> = MutableLiveData()
    private var currentCardsSetPage = 1
    private var isLastPageCardsSet = false
    private var currentSetId: String? = null

    init {
        getLastSetsCardsList()
        getSets()
    }

    fun getLastSetsCardsList(isNextPage: Boolean = false) {
        if (isNextPage && (loadingMoreCards.value == true || isLastPageLastSets)) return
        if (!isNextPage && loadingSetCards.value == true) return

        if (isNextPage) {
            loadingMoreCards.postValue(true)
            currentLastSetsPage++
        } else {
            loadingSetCards.postValue(true)
            currentLastSetsPage = 1
            isLastPageLastSets = false
        }

        viewModelScope.launch {
            try {
                val response = getLastSetsCards(currentLastSetsPage)
                if (response.status == "200") {
                    val currentList = if (isNextPage) setsCardsData.value.orEmpty() else emptyList()
                    val newList = currentList + response.data
                    if (response.data.isEmpty()) isLastPageLastSets = true
                    setsCardsData.postValue(newList)
                }
            } catch (e: Exception) {
                Log.e("SetsViewModels", "Error fetching last sets cards: ${e.message}")
            } finally {
                loadingSetCards.postValue(false)
                loadingMoreCards.postValue(false)
            }
        }
    }

    fun getCardsSet(setId: String, isNextPage: Boolean = false) {

        if (currentSetId != setId) {
            currentSetId = setId
            currentCardsSetPage = 1
            isLastPageCardsSet = false
            cardsSetData.postValue(emptyList())
        }

        if (isNextPage && (loadingMoreCardsSet.value == true || isLastPageCardsSet)) return
        if (!isNextPage && loadingCardsSet.value == true) return

        if (isNextPage) {
            loadingMoreCardsSet.postValue(true)
            currentCardsSetPage++
        } else {
            loadingCardsSet.postValue(true)
            currentCardsSetPage = 1
            isLastPageCardsSet = false
        }

        viewModelScope.launch {
            try {
                val response = getCardsSetList(setId, currentCardsSetPage)
                if (response.status == "200") {
                    val currentList = if (isNextPage) cardsSetData.value.orEmpty() else emptyList()
                    val newList = currentList + response.data
                    if (response.data.isEmpty()) isLastPageCardsSet = true
                    cardsSetData.postValue(newList)
                }
            } catch (e: Exception) {
                Log.e("SetsViewModels cards set", "Error: ${e.message}")
            } finally {
                loadingCardsSet.postValue(false)
                loadingMoreCardsSet.postValue(false)
            }
        }
    }

    fun getSets() {
        loadingSetList.postValue(true)
        viewModelScope.launch {
            try {
                val response = getSetsList()
                if (response.status == "200") loadingSetList.postValue(false)
                setsListData.postValue(response.data)
            } catch (e: Exception) {
                Log.e("SetsViewModels", "Error: ${e.message}")
                loadingSetList.postValue(false)
            }
        }
    }

    fun searchSets(query: String) {
        loadingSetList.postValue(true)
        viewModelScope.launch {
            try {
                val response = getSearchSetUseCase(query)
                if (response.status == "200") loadingSetList.postValue(false)
                setsListData.postValue(response.data)
            } catch (e: Exception) {
                Log.e("SetsViewModels", "Error: ${e.message}")
                loadingSetList.postValue(false)
            }
        }
    }
}
