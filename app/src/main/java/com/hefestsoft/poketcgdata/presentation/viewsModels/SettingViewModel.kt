package com.hefestsoft.poketcgdata.presentation.viewsModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hefestsoft.poketcgdata.core.AppPreferencesManager
import com.hefestsoft.poketcgdata.data.repositories.LanguagePreferenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val languagePreferenceRepository: LanguagePreferenceRepository
) : ViewModel() {

    val selectedLanguage = MutableLiveData(languagePreferenceRepository.getSavedLanguage())
    val isSaving = MutableLiveData(false)
    val errorMessage = MutableLiveData<String?>(null)
    val savedLanguageEvent = MutableLiveData<String?>(null)
    val deviceId = MutableLiveData(languagePreferenceRepository.getOrCreateDeviceId())

    val supportedLanguages = listOf(
        "en",
        "es",


    )

    fun selectLanguage(languageCode: String) {
        selectedLanguage.value = languageCode
    }

    fun saveLanguage() {
        val languageCode = selectedLanguage.value ?: AppPreferencesManager.DEFAULT_LANGUAGE
        errorMessage.value = null

        viewModelScope.launch {
            isSaving.postValue(true)
            try {
                languagePreferenceRepository.setLanguage(languageCode)
                savedLanguageEvent.postValue(languageCode)
            } catch (exception: Exception) {
                errorMessage.postValue(exception.message ?: "Unable to save language")
            } finally {
                isSaving.postValue(false)
            }
        }
    }

    fun consumeSavedLanguageEvent() {
        savedLanguageEvent.value = null
    }
}
