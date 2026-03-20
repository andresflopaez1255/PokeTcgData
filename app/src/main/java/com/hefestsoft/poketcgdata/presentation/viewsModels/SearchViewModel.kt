package com.hefestsoft.poketcgdata.presentation.viewsModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hefestsoft.poketcgdata.data.dtos.CardQuery
import com.hefestsoft.poketcgdata.data.dtos.CardResumeDto
import com.hefestsoft.poketcgdata.data.dtos.SearchMetadataDto
import com.hefestsoft.poketcgdata.data.repositories.CardsRepository
import com.hefestsoft.poketcgdata.data.repositories.MetadataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: CardsRepository,
    private val metadataRepository: MetadataRepository
) : ViewModel() {
    val resultsSearchCards : MutableLiveData<List<CardResumeDto>> = MutableLiveData()
    val loadingSearchCard : MutableLiveData<Boolean> = MutableLiveData()
    val filtersMetadata: MutableLiveData<SearchFiltersMetadataUiState> =
        MutableLiveData(SearchFiltersMetadataUiState(isLoading = false))


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

    fun loadFiltersMetadata(forceRefresh: Boolean = false) {
        val currentState = filtersMetadata.value
        if (currentState?.isLoading == true) return
        if (!forceRefresh && currentState?.metadata != null) return

        viewModelScope.launch {
            filtersMetadata.postValue(
                SearchFiltersMetadataUiState(
                    isLoading = true,
                    metadata = currentState?.metadata
                )
            )

            try {
                val metadata = metadataRepository.getSearchMetadata(forceRefresh)
                filtersMetadata.postValue(
                    SearchFiltersMetadataUiState(
                        isLoading = false,
                        metadata = metadata
                    )
                )
            } catch (exception: Exception) {
                filtersMetadata.postValue(
                    SearchFiltersMetadataUiState(
                        isLoading = false,
                        metadata = currentState?.metadata,
                        errorMessage = exception.message ?: "Unable to load filters"
                    )
                )
            }
        }
    }
}

data class SearchFiltersMetadataUiState(
    val isLoading: Boolean,
    val metadata: SearchMetadataDto? = null,
    val errorMessage: String? = null
)
