package com.open.iptv.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.open.iptv.data.AppSettings
import com.open.iptv.data.EpgDefaults
import com.open.iptv.data.PlaylistRepository
import com.open.iptv.data.SettingsRepository
import com.open.iptv.domain.model.Channel
import com.open.iptv.domain.model.Program
import com.open.iptv.player.PlayerController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = PlaylistRepository()
    private val settingsRepository = SettingsRepository(application)
    private val playerController = PlayerController(application)

    private val _channels = MutableStateFlow<List<Channel>>(emptyList())
    val channels: StateFlow<List<Channel>> = _channels.asStateFlow()

    private val _groups = MutableStateFlow<List<String>>(emptyList())
    val groups: StateFlow<List<String>> = _groups.asStateFlow()

    private val _favorites = MutableStateFlow<Set<String>>(emptySet())
    val favorites: StateFlow<Set<String>> = _favorites.asStateFlow()

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _selectedGroup = MutableStateFlow<String?>(null)
    val selectedGroup: StateFlow<String?> = _selectedGroup.asStateFlow()

    private val _selectedChannel = MutableStateFlow<Channel?>(null)
    val selectedChannel: StateFlow<Channel?> = _selectedChannel.asStateFlow()

    private val _status = MutableStateFlow<String?>(null)
    val status: StateFlow<String?> = _status.asStateFlow()

    private val _programs = MutableStateFlow<List<Program>>(emptyList())
    val programs: StateFlow<List<Program>> = _programs.asStateFlow()

    private val _settings = MutableStateFlow(AppSettings())
    val settings: StateFlow<AppSettings> = _settings.asStateFlow()

    private val _recordings = MutableStateFlow<Set<String>>(emptySet())
    val recordings: StateFlow<Set<String>> = _recordings.asStateFlow()

    private val _recentChannelIds = MutableStateFlow<List<String>>(emptyList())
    val recentChannelIds: StateFlow<List<String>> = _recentChannelIds.asStateFlow()

    val player = playerController.player

    init {
        viewModelScope.launch {
            _channels.collect { list ->
                _groups.value = list.map { it.group }
                    .filter { it.isNotBlank() }
                    .distinct()
                    .sorted()
            }
        }
        var initialLoaded = false
        viewModelScope.launch {
            settingsRepository.settings.collect { value ->
                _settings.value = value
                if (!initialLoaded) {
                    initialLoaded = true
                    if (value.playlistUrl.isNotBlank()) loadPlaylist(value.playlistUrl)
                    if (value.epgUrl.isNotBlank()) {
                        loadEpg(value.epgUrl)
                    } else if (value.useDefaultEpg) {
                        loadDefaultEpg()
                    }
                }
            }
        }
        viewModelScope.launch {
            settingsRepository.recordings.collect { _recordings.value = it }
        }
        viewModelScope.launch {
            settingsRepository.recentChannels.collect { _recentChannelIds.value = it }
        }
    }

    fun updateQuery(value: String) {
        _query.value = value
    }

    fun selectGroup(group: String?) {
        _selectedGroup.value = group
    }

    fun loadPlaylist(url: String) {
        viewModelScope.launch {
            _status.value = "Loading playlist…"
            runCatching { repository.load(url) }
                .onSuccess { list ->
                    _channels.value = list
                    _status.value = "Loaded ${list.size} channels"
                }
                .onFailure { throwable ->
                    _status.value = "Could not load playlist: ${throwable.message ?: "unknown error"}"
                }
        }
    }

    fun loadChannelsFromText(text: String) {
        viewModelScope.launch {
            runCatching { repository.loadText(text) }
                .onSuccess { list ->
                    _channels.value = list
                    _status.value = "Loaded ${list.size} channels"
                }
                .onFailure { throwable ->
                    _status.value = "Could not read playlist: ${throwable.message ?: "unknown error"}"
                }
        }
    }

    fun loadEpg(url: String) {
        viewModelScope.launch {
            _status.value = "Loading EPG…"
            runCatching { repository.loadEpg(url) }
                .onSuccess { list ->
                    _programs.value = list
                    _status.value = "Loaded ${list.size} programs"
                }
                .onFailure { throwable ->
                    _status.value = "Could not load EPG: ${throwable.message ?: "unknown error"}"
                }
        }
    }

    fun loadDefaultEpg() {
        viewModelScope.launch {
            _status.value = "Loading built-in EPG…"
            val (programs, failed) = repository.loadEpgSources(EpgDefaults.sources.map { it.url })
            if (programs.isNotEmpty()) _programs.value = programs
            _status.value = when {
                programs.isEmpty() -> "Built-in EPG failed to load"
                failed.isEmpty() -> "Loaded ${programs.size} programs from built-in EPG"
                else -> "Loaded ${programs.size} programs; ${failed.size} source(s) failed"
            }
        }
    }

    fun loadEpgText(text: String) {
        viewModelScope.launch {
            runCatching { repository.parseEpgText(text) }
                .onSuccess { list ->
                    _programs.value = list
                    _status.value = "Loaded ${list.size} programs"
                }
                .onFailure { throwable ->
                    _status.value = "Could not read EPG: ${throwable.message ?: "unknown error"}"
                }
        }
    }

    fun toggleRecording(programId: String) {
        viewModelScope.launch {
            runCatching { settingsRepository.toggleRecording(programId) }
                .onFailure { _status.value = "Could not save recording" }
        }
    }

    fun playRelative(offset: Int) {
        val current = _selectedChannel.value ?: return
        val index = _channels.value.indexOfFirst { it.id == current.id }
        if (index < 0) return
        val next = _channels.value.getOrNull(index + offset) ?: return
        play(next)
    }

    fun toggleCurrentFavorite() {
        _selectedChannel.value?.let { toggleFavorite(it.id) }
    }

    fun saveSettings(playlistUrl: String, epgUrl: String, useDefaultEpg: Boolean) {
        viewModelScope.launch {
            runCatching { settingsRepository.save(playlistUrl, epgUrl, useDefaultEpg) }
                .onSuccess {
                    _settings.value = AppSettings(playlistUrl, epgUrl, useDefaultEpg)
                    _status.value = "Settings saved"
                }
                .onFailure { _status.value = "Could not save settings" }
        }
    }

    fun toggleFavorite(channelId: String) {
        _favorites.value = if (channelId in _favorites.value) {
            _favorites.value - channelId
        } else {
            _favorites.value + channelId
        }
    }

    fun play(channel: Channel) {
        _selectedChannel.value = channel
        playerController.play(channel)
        viewModelScope.launch {
            runCatching { settingsRepository.recordChannel(channel.id) }
        }
    }

    fun togglePlayPause() = playerController.togglePlayPause()

    fun stopPlayback() {
        playerController.stop()
        _selectedChannel.value = null
    }

    fun clearStatus() {
        _status.value = null
    }

    override fun onCleared() {
        playerController.release()
        super.onCleared()
    }
}
