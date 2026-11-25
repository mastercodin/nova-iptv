package com.open.iptv.data

import org.junit.Assert.assertEquals
import org.junit.Test

class XmlTvParserTest {

    @Test
    fun parsesProgrammeEntries() {
        val xml = """
            <?xml version="1.0" encoding="UTF-8"?>
            <tv>
              <channel id="news"><display-name>News</display-name></channel>
              <programme start="20261125100000 +0000" stop="20261125110000 +0000" channel="news">
                <title lang="en">Morning News</title>
                <desc lang="en">Today's headlines</desc>
              </programme>
            </tv>
        """.trimIndent()

        val programs = XmlTvParser.parse(xml)

        assertEquals(1, programs.size)
        assertEquals("news", programs[0].channelId)
        assertEquals("Morning News", programs[0].title)
        assertEquals("20261125100000 +0000", programs[0].start)
        assertEquals("Today's headlines", programs[0].description)
    }
}
