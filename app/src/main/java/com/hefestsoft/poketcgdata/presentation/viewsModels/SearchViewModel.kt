package com.hefestsoft.poketcgdata.presentation.viewsModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hefestsoft.poketcgdata.data.dtos.CardQuery
import com.hefestsoft.poketcgdata.data.dtos.CardResumeDto
import com.hefestsoft.poketcgdata.data.repositories.CardsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: CardsRepository
) : ViewModel() {
    val resultsSearchCards : MutableLiveData<List<CardResumeDto>> = MutableLiveData()
    val loadingSearchCard : MutableLiveData<Boolean> = MutableLiveData()


    suspend fun searchCard(
        cardQuery: CardQuery
    ) {
        try {
            loadingSearchCard.postValue(true)
            val result = repository.searchCards(cardQuery)
            resultsSearchCards.postValue(result.data)
        } catch (e: Exception){
            e.printStackTrace()
        } finally {
            loadingSearchCard.postValue(false)

        }



    }
}
