package com.hefestsoft.poketcgdata.data.repositories

import com.hefestsoft.poketcgdata.core.AppPreferencesManager
import com.hefestsoft.poketcgdata.core.CardsApi
import com.hefestsoft.poketcgdata.data.dtos.LanguagePreferenceRequestDto
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LanguagePreferenceRepository @Inject constructor(
    private val cardsApi: CardsApi,
    private val appPreferencesManager: AppPreferencesManager
) {

    suspend fun setLanguage(languageCode: String) {
        val deviceID = appPreferencesManager.getOrCreateDeviceId()
        val response = cardsApi.setLanguagePreference(
            LanguagePreferenceRequestDto(deviceId = deviceID, lang = languageCode)
        )

        if (!response.isSuccessful) {
            throw IllegalStateException(parseErrorMessage(response))
        }

        appPreferencesManager.saveLanguage(languageCode)
    }

    fun getSavedLanguage(): String = appPreferencesManager.getSavedLanguage()

    fun getOrCreateDeviceId(): String = appPreferencesManager.getOrCreateDeviceId()

    private fun parseErrorMessage(response: Response<Unit>): String {
        val rawError = response.errorBody()?.string()?.trim().orEmpty()
        if (rawError.isBlank()) {
            return "Unable to save language preference (${response.code()})"
        }

        return try {
            val json = JSONObject(rawError)
            when {
                json.optString("message").isNotBlank() -> json.optString("message")
                json.optString("error").isNotBlank() -> json.optString("error")
                else -> rawError
            }
        } catch (_: Exception) {
            rawError
        }
    }
}
