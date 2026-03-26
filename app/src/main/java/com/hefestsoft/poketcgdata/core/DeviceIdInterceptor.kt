package com.hefestsoft.poketcgdata.core

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceIdInterceptor @Inject constructor(
    private val appPreferencesManager: AppPreferencesManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val updatedRequest = chain.request()
            .newBuilder()
            .header("x-device-id", appPreferencesManager.getOrCreateDeviceId())
            .build()

        return chain.proceed(updatedRequest)
    }
}
