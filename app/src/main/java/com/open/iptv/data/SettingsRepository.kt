package com.open.iptv.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.settingsDataStore by preferencesDataStore(name = "open_iptv_settings")

data class AppSettings(
    val playlistUrl: String = "",
    val epgUrl: String = "",
    val useDefaultEpg: Boolean = true,
)

class SettingsRepository(private val context: Context) {

    val settings: Flow<AppSettings> = context.settingsDataStore.data.map { prefs ->
        AppSettings(
            playlistUrl = prefs[PLAYLIST_URL].orEmpty(),
            epgUrl = prefs[EPG_URL].orEmpty(),
            useDefaultEpg = prefs[USE_DEFAULT_EPG] != false,
        )
    }

    val recordings: Flow<Set<String>> = context.settingsDataStore.data.map { prefs ->
        prefs[RECORDED].orEmpty()
    }

    val recentChannels: Flow<List<String>> = context.settingsDataStore.data.map { prefs ->
        prefs[RECENT].orEmpty().split('\n').filter { it.isNotBlank() }
    }

    suspend fun recordChannel(id: String) {
        context.settingsDataStore.edit { prefs ->
            val updated = buildList {
                add(id)
                prefs[RECENT].orEmpty().split('\n').filter { it.isNotBlank() && it != id }
                    .forEach { add(it) }
            }.take(20)
            prefs[RECENT] = updated.joinToString("\n")
        }
    }

    suspend fun save(playlistUrl: String, epgUrl: String, useDefaultEpg: Boolean) {
        context.settingsDataStore.edit { prefs ->
            prefs[PLAYLIST_URL] = playlistUrl
            prefs[EPG_URL] = epgUrl
            prefs[USE_DEFAULT_EPG] = useDefaultEpg
        }
    }

    suspend fun toggleRecording(programId: String) {
        context.settingsDataStore.edit { prefs ->
            val current = prefs[RECORDED].orEmpty()
            prefs[RECORDED] = if (programId in current) current - programId else current + programId
        }
    }

    private companion object {
        val PLAYLIST_URL = stringPreferencesKey("playlist_url")
        val EPG_URL = stringPreferencesKey("epg_url")
        val USE_DEFAULT_EPG = booleanPreferencesKey("use_default_epg")
        val RECORDED = stringSetPreferencesKey("recorded_programs")
        val RECENT = stringPreferencesKey("recent_channels")
    }
}
