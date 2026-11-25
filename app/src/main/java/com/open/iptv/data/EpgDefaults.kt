package com.open.iptv.data

/**
 * Optional built-in EPG sources. These are user-replaceable defaults, not a
 * claim of ownership or authorization over the data. The user can disable
 * them in Settings and supply their own XMLTV sources instead.
 */
object EpgDefaults {

    val sources: List<EpgSource> = listOf(
        EpgSource("ALL OTT EPG", "https://avkb.short.gy/epg.xml.gz"),
        EpgSource("JioTV EPG", "https://avkb.short.gy/jioepg.xml.gz"),
        EpgSource("TataPlay EPG", "https://avkb.short.gy/tsepg.xml.gz"),
    )

    data class EpgSource(
        val name: String,
        val url: String,
    )
}
