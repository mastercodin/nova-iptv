package com.open.iptv.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.compose.PlayerSurface
import com.open.iptv.domain.model.Channel

/** Full-screen playback surface with remote-friendly controls. */
@Composable
fun PlayerOverlay(
    channel: Channel,
    player: ExoPlayer,
    onClose: () -> Unit,
    onTogglePlayPause: () -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onToggleFavorite: () -> Unit,
    isFavorite: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.background(Color.Black)) {
        PlayerSurface(player = player, modifier = Modifier.fillMaxSize())

        Box(modifier = Modifier.fillMaxSize().safeDrawingPadding()) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = channel.name,
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f),
                )
                IconButton(onClick = onToggleFavorite) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
                        contentDescription = if (isFavorite) "Remove favorite" else "Add favorite",
                        tint = Color.White,
                    )
                }
                IconButton(onClick = onClose) {
                    Icon(Icons.Default.Close, contentDescription = "Close player", tint = Color.White)
                }
            }

            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = onPrevious) {
                    Icon(Icons.Default.SkipPrevious, contentDescription = "Previous channel", tint = Color.White)
                }
                FilledTonalButton(onClick = onTogglePlayPause) {
                    Icon(
                        imageVector = if (player.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = null,
                    )
                    Text(
                        text = if (player.isPlaying) "Pause" else "Play",
                        modifier = Modifier.padding(start = 8.dp),
                    )
                }
                IconButton(onClick = onNext) {
                    Icon(Icons.Default.SkipNext, contentDescription = "Next channel", tint = Color.White)
                }
            }
        }
    }
}
