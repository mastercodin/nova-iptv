package com.open.iptv.data

import com.open.iptv.domain.model.Channel
import com.open.iptv.domain.model.Program
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.ByteArrayInputStream
import java.util.zip.GZIPInputStream

/** Loads user-supplied playlists and EPG data. The app never bundles provider content. */
class PlaylistRepository(
    private val client: OkHttpClient = OkHttpClient(),
) {

    suspend fun load(url: String): List<Channel> = withContext(Dispatchers.IO) {
        val request = Request.Builder().url(url).build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) error("Playlist request failed with HTTP ${response.code}")
            parsePlaylist(decodeBody(response.body!!.bytes()))
        }
    }

    suspend fun loadText(body: String): List<Channel> = withContext(Dispatchers.Default) {
        parsePlaylist(body)
    }

    suspend fun parseEpgText(text: String): List<Program> = withContext(Dispatchers.Default) {
        XmlTvParser.parse(text)
    }

    suspend fun loadEpg(url: String): List<Program> = withContext(Dispatchers.IO) {
        val request = Request.Builder().url(url).build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) error("EPG request failed with HTTP ${response.code}")
            XmlTvParser.parse(decodeBody(response.body!!.bytes()))
        }
    }

    /** Loads several EPG sources, keeping successful ones and reporting failed URLs. */
    suspend fun loadEpgSources(urls: List<String>): Pair<List<Program>, List<String>> = withContext(Dispatchers.IO) {
        val programs = mutableListOf<Program>()
        val failed = mutableListOf<String>()
        urls.forEach { url ->
            runCatching { loadEpg(url) }
                .onSuccess { programs.addAll(it) }
                .onFailure { failed.add(url) }
        }
        programs to failed
    }

    private fun parsePlaylist(body: String): List<Channel> {
        if (body.isBlank()) error("Playlist is empty")
        return M3uParser.parse(body)
    }

    /** Handles plain UTF-8 and gzip responses even when no Content-Encoding header is set. */
    private fun decodeBody(bytes: ByteArray): String {
        if (bytes.size >= 2 && bytes[0] == 0x1f.toByte() && bytes[1] == 0x8b.toByte()) {
            GZIPInputStream(ByteArrayInputStream(bytes)).use { stream ->
                return stream.readBytes().decodeToString()
            }
        }
        return bytes.decodeToString()
    }
}
