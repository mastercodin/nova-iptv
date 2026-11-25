package com.open.iptv.player

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.open.iptv.domain.model.Channel

/** Thin wrapper around ExoPlayer so UI code never owns player lifecycle. */
class PlayerController(context: Context) {

    val player: ExoPlayer = ExoPlayer.Builder(context.applicationContext).build()

    fun play(channel: Channel) {
        player.stop()
        player.clearMediaItems()
        player.setMediaItem(MediaItem.fromUri(channel.url))
        player.prepare()
        player.playWhenReady = true
    }

    fun togglePlayPause() {
        if (player.isPlaying) player.pause() else player.play()
    }

    fun stop() {
        player.stop()
        player.clearMediaItems()
    }

    fun release() {
        player.release()
    }
}
