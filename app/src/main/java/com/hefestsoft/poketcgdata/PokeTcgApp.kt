package com.hefestsoft.poketcgdata

import android.app.Application
import com.hefestsoft.poketcgdata.core.AppPreferencesManager
import com.hefestsoft.poketcgdata.core.LocaleManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class PokeTcgApp : Application() {

    @Inject
    lateinit var appPreferencesManager: AppPreferencesManager

    @Inject
    lateinit var localeManager: LocaleManager

    override fun onCreate() {
        super.onCreate()
        localeManager.applyLanguage(appPreferencesManager.getSavedLanguage())
        appPreferencesManager.getOrCreateDeviceId()
    }
}
