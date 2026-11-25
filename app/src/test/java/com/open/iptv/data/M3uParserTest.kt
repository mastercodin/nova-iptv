package com.open.iptv.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class M3uParserTest {

    @Test
    fun parsesExtinfAttributesAndUrl() {
        val playlist = """
            #EXTM3U
            #EXTINF:-1 tvg-id="cnn.us" tvg-logo="https://example.com/cnn.png" group-title="News",CNN
            https://example.com/cnn.m3u8
            #EXTINF:-1 tvg-id="discovery" group-title="Documentary",Discovery
            https://example.com/discovery.ts
        """.trimIndent()

        val channels = M3uParser.parse(playlist)

        assertEquals(2, channels.size)
        assertEquals("CNN", channels[0].name)
        assertEquals("cnn.us", channels[0].id)
        assertEquals("News", channels[0].group)
        assertEquals("https://example.com/cnn.png", channels[0].logoUrl)
        assertEquals("https://example.com/cnn.m3u8", channels[0].url)
        assertEquals("Documentary", channels[1].group)
        assertNull(channels[1].logoUrl)
    }

    @Test
    fun ignoresCommentsAndSkipsHeader() {
        val playlist = """
            #EXTM3U
            # comment
            https://example.com/no-metadata.ts
        """.trimIndent()

        val channels = M3uParser.parse(playlist)

        assertEquals(1, channels.size)
        assertEquals("Channel 1", channels[0].name)
    }
}
