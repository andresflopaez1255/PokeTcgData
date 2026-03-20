package com.hefestsoft.poketcgdata.data.repositories

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hefestsoft.poketcgdata.core.CardsApi
import com.hefestsoft.poketcgdata.data.dtos.MetadataSetDto
import com.hefestsoft.poketcgdata.data.dtos.SearchMetadataDto
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MetadataRepository @Inject constructor(
    private val api: CardsApi,
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) {

    suspend fun getSearchMetadata(forceRefresh: Boolean = false): SearchMetadataDto = coroutineScope {
        val rarities = async {
            getCachedStringList(CACHE_RARITIES, forceRefresh) { api.getRarities() }
        }
        val types = async {
            getCachedStringList(CACHE_TYPES, forceRefresh) { api.getTypes() }
        }
        val cardTypes = async {
            getCachedStringList(CACHE_CARD_TYPES, forceRefresh) { api.getCardTypes() }
        }
        val sets = async {
            getCachedSets(forceRefresh)
        }

        SearchMetadataDto(
            rarities = rarities.await(),
            types = types.await(),
            cardTypes = cardTypes.await(),
            sets = sets.await()
        )
    }

    private suspend fun getCachedStringList(
        key: String,
        forceRefresh: Boolean,
        fetcher: suspend () -> List<String>
    ): List<String> {
        val cached = readStringList(key)
        if (!forceRefresh && isFresh(key) && cached != null) {
            return cached
        }

        return try {
            val remote = fetcher().filter { it.isNotBlank() }.distinct()
            saveValue(key, remote)
            remote
        } catch (exception: Exception) {
            cached ?: throw exception
        }
    }

    private suspend fun getCachedSets(forceRefresh: Boolean): List<MetadataSetDto> {
        val cached = readSets(CACHE_SETS)
        if (!forceRefresh && isFresh(CACHE_SETS) && cached != null) {
            return cached
        }

        return try {
            val remote = api.getMetadataSets()
                .data
                .filter { it.id.isNotBlank() && it.name.isNotBlank() }
                .distinctBy { it.id }
            saveValue(CACHE_SETS, remote)
            remote
        } catch (exception: Exception) {
            cached ?: throw exception
        }
    }

    private fun readStringList(key: String): List<String>? {
        val json = sharedPreferences.getString(key, null) ?: return null
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(json, type)
    }

    private fun readSets(key: String): List<MetadataSetDto>? {
        val json = sharedPreferences.getString(key, null) ?: return null
        val type = object : TypeToken<List<MetadataSetDto>>() {}.type
        return gson.fromJson(json, type)
    }

    private fun saveValue(key: String, value: Any) {
        sharedPreferences.edit()
            .putString(key, gson.toJson(value))
            .putLong("$key$TIMESTAMP_SUFFIX", System.currentTimeMillis())
            .apply()
    }

    private fun isFresh(key: String): Boolean {
        val cachedAt = sharedPreferences.getLong("$key$TIMESTAMP_SUFFIX", 0L)
        return cachedAt != 0L && System.currentTimeMillis() - cachedAt <= CACHE_TTL_MS
    }

    companion object {
        private const val CACHE_TTL_MS = 24 * 60 * 60 * 1000L
        private const val TIMESTAMP_SUFFIX = "_timestamp"
        private const val CACHE_RARITIES = "metadata_rarities"
        private const val CACHE_TYPES = "metadata_types"
        private const val CACHE_CARD_TYPES = "metadata_card_types"
        private const val CACHE_SETS = "metadata_sets"
    }
}
