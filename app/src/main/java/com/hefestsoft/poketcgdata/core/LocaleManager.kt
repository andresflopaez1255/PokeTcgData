package com.hefestsoft.poketcgdata.core

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocaleManager @Inject constructor() {

    fun applyLanguage(languageCode: String) {
        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.forLanguageTags(languageCode)
        )
    }
}
