package com.open.iptv.domain.model

/** One schedule entry from an XMLTV EPG feed. */
data class Program(
    val id: String,
    val channelId: String,
    val title: String,
    val start: String,
    val stop: String,
    val description: String = "",
)
