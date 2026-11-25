package com.open.iptv.domain.model

/**
 * A single playable channel parsed from an M3U playlist.
 *
 * This is an original model for the clean-room app; it does not mirror any
 * proprietary application's data structures.
 */
data class Channel(
    val id: String,
    val name: String,
    val url: String,
    val group: String = "",
    val logoUrl: String? = null,
)
