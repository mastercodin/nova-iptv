package com.open.iptv.ui

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items as gridItems
import androidx.compose.foundation.lazy.items as lazyItems
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.open.iptv.data.AppSettings
import com.open.iptv.data.EpgDefaults
import com.open.iptv.domain.model.Channel
import com.open.iptv.domain.model.Program
import com.open.iptv.player.PlayerOverlay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MainViewModel = viewModel()) {
    val channels by viewModel.channels.collectAsStateWithLifecycle()
    val favorites by viewModel.favorites.collectAsStateWithLifecycle()
    val query by viewModel.query.collectAsStateWithLifecycle()
    val selectedGroup by viewModel.selectedGroup.collectAsStateWithLifecycle()
    val selectedChannel by viewModel.selectedChannel.collectAsStateWithLifecycle()
    val status by viewModel.status.collectAsStateWithLifecycle()
    val groups by viewModel.groups.collectAsStateWithLifecycle()
    val programs by viewModel.programs.collectAsStateWithLifecycle()
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val recordings by viewModel.recordings.collectAsStateWithLifecycle()
    val recentIds by viewModel.recentChannelIds.collectAsStateWithLifecycle()

    var destination by rememberSaveable { mutableIntStateOf(0) }
    var showPlaylistDialog by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val openPlaylist = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        uri?.let {
            context.contentResolver.openInputStream(it)?.use { input ->
                viewModel.loadChannelsFromText(input.readBytes().decodeToString())
            }
        }
    }
    val openEpg = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        uri?.let {
            context.contentResolver.openInputStream(it)?.use { input ->
                viewModel.loadEpgText(input.readBytes().decodeToString())
            }
        }
    }

    val configuration = LocalConfiguration.current
    val isWideScreen = configuration.screenWidthDp >= 600
    val cardMinWidth = if (isWideScreen) 220.dp else 132.dp
    val screenPadding = if (isWideScreen) 24.dp else 12.dp

    LaunchedEffect(status) {
        status?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearStatus()
        }
    }

    BackHandler(enabled = selectedChannel != null) {
        viewModel.stopPlayback()
    }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            item(
                selected = destination == 0,
                onClick = { destination = 0 },
                icon = { Icon(Icons.Default.LiveTv, contentDescription = null) },
                label = { Text("Channels") },
            )
            item(
                selected = destination == 1,
                onClick = { destination = 1 },
                icon = { Icon(Icons.Default.Favorite, contentDescription = null) },
                label = { Text("Favorites") },
            )
            item(
                selected = destination == 2,
                onClick = { destination = 2 },
                icon = { Icon(Icons.Default.CalendarMonth, contentDescription = null) },
                label = { Text("Guide") },
            )
            item(
                selected = destination == 3,
                onClick = { destination = 3 },
                icon = { Icon(Icons.Default.Bookmark, contentDescription = null) },
                label = { Text("Recordings") },
            )
        },
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            when (destination) {
                                1 -> "Favorites"
                                2 -> "Guide"
                                3 -> "Recordings"
                                else -> "Open IPTV"
                            }
                        )
                    },
                    actions = {
                        IconButton(onClick = { showSettingsDialog = true }) {
                            Icon(Icons.Default.Settings, contentDescription = "Settings")
                        }
                        IconButton(onClick = { showPlaylistDialog = true }) {
                            Icon(Icons.Default.Add, contentDescription = "Add playlist")
                        }
                    },
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
        ) { innerPadding ->
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                ) {
                    when (destination) {
                        2 -> GuideScreen(
                            programs = programs,
                            recordings = recordings,
                            onToggleRecording = viewModel::toggleRecording,
                            onOpenSettings = { showSettingsDialog = true },
                        )
                        3 -> RecordingsScreen(
                            programs = programs.filter { it.id in recordings },
                            onRemove = viewModel::toggleRecording,
                            onOpenGuide = { destination = 2 },
                        )
                        else -> {
                    OutlinedTextField(
                        value = query,
                        onValueChange = viewModel::updateQuery,
                        label = { Text("Search channels") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                    )

                    val recentChannels = recentIds.mapNotNull { id -> channels.find { it.id == id } }
                    if (recentChannels.isNotEmpty()) {
                        Text(
                            "Recently played",
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                        )
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            lazyItems(recentChannels, key = { it.id }, contentType = { "recent" }) { channel ->
                                FilterChip(
                                    selected = false,
                                    onClick = { viewModel.play(channel) },
                                    label = { Text(channel.name) },
                                )
                            }
                        }
                    }

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        item {
                            FilterChip(
                                selected = selectedGroup == null,
                                onClick = { viewModel.selectGroup(null) },
                                label = { Text("All") },
                            )
                        }
                        lazyItems(groups) { group ->
                            FilterChip(
                                selected = selectedGroup == group,
                                onClick = { viewModel.selectGroup(group) },
                                label = { Text(group) },
                            )
                        }
                    }

                    val visibleChannels = channels
                        .filter { channel -> destination != 1 || channel.id in favorites }
                        .filter { channel -> selectedGroup == null || channel.group == selectedGroup }
                        .filter { channel ->
                            query.isBlank() || channel.name.contains(query, ignoreCase = true)
                        }

                    AnimatedContent(targetState = visibleChannels.isEmpty()) { isEmpty ->
                        if (isEmpty) {
                            EmptyState(onAdd = { showPlaylistDialog = true })
                        } else {
                            LazyVerticalGrid(
                                columns = GridCells.Adaptive(cardMinWidth),
                                contentPadding = PaddingValues(screenPadding),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.fillMaxSize(),
                            ) {
                                gridItems(
                                    items = visibleChannels,
                                    key = { it.id },
                                    contentType = { "channel" },
                                ) { channel ->
                                    ChannelCard(
                                        channel = channel,
                                        isFavorite = channel.id in favorites,
                                        isTv = isWideScreen,
                                        onPlay = { viewModel.play(channel) },
                                        onToggleFavorite = { viewModel.toggleFavorite(channel.id) },
                                    )
                                }
                            }
                        }
                    }
                    }
                    }
                }

                selectedChannel?.let { channel ->
                    PlayerOverlay(
                        channel = channel,
                        player = viewModel.player,
                        onClose = viewModel::stopPlayback,
                        onTogglePlayPause = viewModel::togglePlayPause,
                        onPrevious = { viewModel.playRelative(-1) },
                        onNext = { viewModel.playRelative(1) },
                        onToggleFavorite = viewModel::toggleCurrentFavorite,
                        isFavorite = channel.id in favorites,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
        }
    }

    if (showPlaylistDialog) {
        PlaylistDialog(
            onDismiss = { showPlaylistDialog = false },
            onLoad = { url ->
                showPlaylistDialog = false
                viewModel.loadPlaylist(url)
            },
            onPickFile = {
                showPlaylistDialog = false
                openPlaylist.launch(arrayOf("*/*"))
            },
        )
    }

    if (showSettingsDialog) {
        SettingsDialog(
            settings = settings,
            onDismiss = { showSettingsDialog = false },
            onPickEpg = {
                showSettingsDialog = false
                openEpg.launch(arrayOf("*/*"))
            },
            onSave = { playlistUrl, epgUrl, useDefaultEpg ->
                viewModel.saveSettings(playlistUrl, epgUrl, useDefaultEpg)
                if (playlistUrl.isNotBlank()) viewModel.loadPlaylist(playlistUrl)
                if (epgUrl.isNotBlank()) {
                    viewModel.loadEpg(epgUrl)
                } else if (useDefaultEpg) {
                    viewModel.loadDefaultEpg()
                }
                showSettingsDialog = false
            },
        )
    }
}

@Composable
private fun EmptyState(onAdd: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            Icons.Default.LiveTv,
            contentDescription = null,
            modifier = Modifier.height(64.dp),
        )
        Spacer(Modifier.height(16.dp))
        Text("No channels yet", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        Text(
            "Add an M3U playlist URL from your provider to get started.",
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(Modifier.height(16.dp))
        TextButton(onClick = onAdd) { Text("Add playlist") }
    }
}

@Composable
private fun ChannelCard(
    channel: Channel,
    isFavorite: Boolean,
    isTv: Boolean,
    onPlay: () -> Unit,
    onToggleFavorite: () -> Unit,
) {
    Card(
        onClick = onPlay,
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = if (isFavorite) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceContainerLow
            },
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(if (isTv) 148.dp else 112.dp),
    ) {
        Box(Modifier.fillMaxSize()) {
            if (channel.logoUrl != null) {
                AsyncImage(
                    model = channel.logoUrl,
                    contentDescription = channel.name,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                )
            } else {
                Icon(
                    Icons.Default.LiveTv,
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .height(40.dp),
                )
            }

            Text(
                text = channel.name,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp),
            )

            IconButton(
                onClick = onToggleFavorite,
                modifier = Modifier.align(Alignment.TopEnd),
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
                    contentDescription = if (isFavorite) "Remove favorite" else "Add favorite",
                )
            }
        }
    }
}

@Composable
private fun GuideScreen(
    programs: List<Program>,
    recordings: Set<String>,
    onToggleRecording: (String) -> Unit,
    onOpenSettings: () -> Unit,
) {
    if (programs.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(Icons.Default.CalendarMonth, contentDescription = null, modifier = Modifier.height(64.dp))
            Spacer(Modifier.height(16.dp))
            Text("No EPG data yet", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))
            Text("Add an XMLTV URL in Settings to see the guide.", style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(16.dp))
            TextButton(onClick = onOpenSettings) { Text("Open settings") }
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        lazyItems(programs, key = { it.id }, contentType = { "program" }) { program ->
            val recorded = program.id in recordings
            Card(
                onClick = { onToggleRecording(program.id) },
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(
                    containerColor = if (recorded) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.surfaceContainerLow
                    },
                ),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(Modifier.padding(12.dp)) {
                    Text(
                        "${program.start} – ${program.stop}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Text(program.title, style = MaterialTheme.typography.titleMedium)
                    if (program.description.isNotBlank()) {
                        Text(
                            program.description,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                    Text(
                        if (recorded) "Tap to remove recording" else "Tap to record",
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
            }
        }
    }
}

@Composable
private fun RecordingsScreen(
    programs: List<Program>,
    onRemove: (String) -> Unit,
    onOpenGuide: () -> Unit,
) {
    if (programs.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(Icons.Default.Bookmark, contentDescription = null, modifier = Modifier.height(64.dp))
            Spacer(Modifier.height(16.dp))
            Text("No recordings scheduled", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))
            Text("Tap a program in the Guide to schedule it.", style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(16.dp))
            TextButton(onClick = onOpenGuide) { Text("Open guide") }
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        lazyItems(programs, key = { it.id }, contentType = { "recording" }) { program ->
            Card(
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(program.title, style = MaterialTheme.typography.titleMedium)
                        Text("${program.start} – ${program.stop}", style = MaterialTheme.typography.labelMedium)
                    }
                    IconButton(onClick = { onRemove(program.id) }) {
                        Icon(Icons.Default.Bookmark, contentDescription = "Remove recording")
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsDialog(
    settings: AppSettings,
    onDismiss: () -> Unit,
    onPickEpg: () -> Unit,
    onSave: (String, String, Boolean) -> Unit,
) {
    var playlistUrl by remember(settings.playlistUrl) { mutableStateOf(settings.playlistUrl) }
    var epgUrl by remember(settings.epgUrl) { mutableStateOf(settings.epgUrl) }
    var useDefaultEpg by remember(settings.useDefaultEpg) { mutableStateOf(settings.useDefaultEpg) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Settings") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = playlistUrl,
                    onValueChange = { playlistUrl = it },
                    label = { Text("M3U playlist URL") },
                    singleLine = true,
                )
                OutlinedTextField(
                    value = epgUrl,
                    onValueChange = { epgUrl = it },
                    label = { Text("XMLTV EPG URL") },
                    singleLine = true,
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = useDefaultEpg, onCheckedChange = { useDefaultEpg = it })
                    Text("Use built-in EPG sources when no URL is set")
                }
                EpgDefaults.sources.forEach { source ->
                    Text("${source.name}: ${source.url}", style = MaterialTheme.typography.labelSmall)
                }
                TextButton(onClick = onPickEpg) { Text("Choose local EPG file") }
            }
        },
        confirmButton = { TextButton(onClick = { onSave(playlistUrl, epgUrl, useDefaultEpg) }) { Text("Save") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
    )
}

@Composable
private fun PlaylistDialog(
    onDismiss: () -> Unit,
    onLoad: (String) -> Unit,
    onPickFile: () -> Unit,
) {
    var url by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add playlist") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = url,
                    onValueChange = { url = it },
                    label = { Text("M3U URL") },
                    placeholder = { Text("https://example.com/playlist.m3u") },
                    singleLine = true,
                )
                TextButton(onClick = onPickFile) { Text("Choose local file") }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onLoad(url) },
                enabled = url.startsWith("http://") || url.startsWith("https://"),
            ) {
                Text("Load")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
    )
}
