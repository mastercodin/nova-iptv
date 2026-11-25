package com.open.iptv.data

import com.open.iptv.domain.model.Channel

/**
 * Minimal, dependency-free M3U parser for user-supplied playlists.
 *
 * Supports the common #EXTINF form:
 *   #EXTINF:-1 tvg-id="cnn.us" tvg-logo="..." group-title="News",CNN
 *   http://example.com/stream.m3u8
 */
object M3uParser {

    fun parse(input: String): List<Channel> {
        val lines = input.lineSequence()
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .toList()

        val channels = mutableListOf<Channel>()
        var pendingName: String? = null
        var pendingGroup = ""
        var pendingLogo: String? = null
        var pendingId: String? = null

        for (line in lines) {
            when {
                line.startsWith("#EXTINF") -> {
                    val comma = line.indexOf(',')
                    val attributes = if (comma >= 0) line.substring(0, comma) else line
                    pendingName = if (comma >= 0 && comma < line.length - 1) {
                        line.substring(comma + 1).trim()
                    } else {
                        "Channel ${channels.size + 1}"
                    }
                    pendingGroup = attribute(attributes, "group-title").orEmpty()
                    pendingLogo = attribute(attributes, "tvg-logo")
                    pendingId = attribute(attributes, "tvg-id")
                        ?: attribute(attributes, "tvg-name")
                        ?: pendingName
                }

                !line.startsWith("#") -> {
                    val name = pendingName ?: "Channel ${channels.size + 1}"
                    channels += Channel(
                        id = pendingId ?: name,
                        name = name,
                        url = line,
                        group = pendingGroup,
                        logoUrl = pendingLogo,
                    )
                    pendingName = null
                    pendingGroup = ""
                    pendingLogo = null
                    pendingId = null
                }
            }
        }

        return channels
    }

    private fun attribute(attributes: String, key: String): String? {
        val marker = "$key=\""
        val start = attributes.indexOf(marker)
        if (start < 0) return null
        val valueStart = start + marker.length
        val end = attributes.indexOf('"', valueStart)
        return if (end > valueStart) attributes.substring(valueStart, end).ifEmpty { null } else null
    }
}
