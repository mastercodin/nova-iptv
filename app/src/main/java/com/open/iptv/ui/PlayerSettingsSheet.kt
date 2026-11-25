package com.open.iptv.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.media3.common.C
import androidx.media3.common.TrackSelectionOverride
import androidx.media3.exoplayer.ExoPlayer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerSettingsSheet(
    player: ExoPlayer,
    speed: Float,
    sleepLabel: String?,
    onDismiss: () -> Unit,
    onSpeed: (Float) -> Unit,
    onSleep: (Int) -> Unit,
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text("Playback speed", style = MaterialTheme.typography.titleMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(0.5f, 0.75f, 1f, 1.25f, 1.5f, 2f).forEach { value ->
                    FilterChip(
                        selected = speed == value,
                        onClick = { onSpeed(value) },
                        label = { Text("${value}x") },
                    )
                }
            }

            Text("Sleep timer${if (sleepLabel != null) "  ·  $sleepLabel" else ""}", style = MaterialTheme.typography.titleMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(0, 5, 15, 30).forEach { minutes ->
                    FilterChip(
                        selected = (minutes == 0 && sleepLabel == null) ||
                            (minutes > 0 && sleepLabel == "Sleep in ${minutes}m"),
                        onClick = { onSleep(minutes) },
                        label = { Text(if (minutes == 0) "Off" else "${minutes}m") },
                    )
                }
            }

            Text("Tracks", style = MaterialTheme.typography.titleMedium)
            player.currentTracks.groups.forEach { group ->
                val kind = when (group.type) {
                    C.TRACK_TYPE_AUDIO -> "Audio"
                    C.TRACK_TYPE_TEXT -> "Subtitles"
                    else -> "Video"
                }
                Text(kind, style = MaterialTheme.typography.labelMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    for (index in 0 until group.length) {
                        val format = group.getTrackFormat(index)
                        val label = format.label ?: format.language ?: "Track ${index + 1}"
                        FilterChip(
                            selected = group.isTrackSelected(index),
                            onClick = {
                                player.trackSelectionParameters = player.trackSelectionParameters
                                    .buildUpon()
                                    .setOverrideForType(
                                        TrackSelectionOverride(group.mediaTrackGroup, index)
                                    )
                                    .build()
                            },
                            label = { Text(label) },
                        )
                    }
                }
            }
        }
    }
}
