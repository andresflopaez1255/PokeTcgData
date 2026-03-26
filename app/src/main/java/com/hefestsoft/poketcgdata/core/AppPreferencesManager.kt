package com.hefestsoft.poketcgdata.core

import android.content.SharedPreferences
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.content.edit

@Singleton
class AppPreferencesManager @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    fun getOrCreateDeviceId(): String {
        val existingDeviceId = sharedPreferences.getString(KEY_DEVICE_ID, null)
        if (!existingDeviceId.isNullOrBlank()) {
            return existingDeviceId
        }

        val generatedDeviceId = UUID.randomUUID().toString()
        sharedPreferences.edit {
            putString(KEY_DEVICE_ID, generatedDeviceId)
        }
        return generatedDeviceId
    }

    fun getSavedLanguage(): String {
        return sharedPreferences.getString(KEY_LANGUAGE, DEFAULT_LANGUAGE) ?: DEFAULT_LANGUAGE
    }

    fun saveLanguage(languageCode: String) {
        sharedPreferences.edit {
            putString(KEY_LANGUAGE, languageCode)
        }
    }

    companion object {
        const val DEFAULT_LANGUAGE = "en"
        private const val KEY_DEVICE_ID = "device_id"
        private const val KEY_LANGUAGE = "language"
    }
}
