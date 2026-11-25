package com.open.iptv.data

import com.open.iptv.domain.model.Program
import org.xml.sax.InputSource
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory

/** Minimal, safe XMLTV parser for user-supplied EPG feeds. */
object XmlTvParser {

    fun parse(xml: String): List<Program> {
        val factory = DocumentBuilderFactory.newInstance().apply {
            isNamespaceAware = false
            isCoalescing = true
            setFeature("http://apache.org/xml/features/disallow-doctype-decl", true)
            setFeature("http://xml.org/sax/features/external-general-entities", false)
            setFeature("http://xml.org/sax/features/external-parameter-entities", false)
        }
        val document = factory.newDocumentBuilder().parse(InputSource(StringReader(xml)))
        val nodes = document.getElementsByTagName("programme")
        val programs = mutableListOf<Program>()

        for (i in 0 until nodes.length) {
            val node = nodes.item(i)
            val element = node as? org.w3c.dom.Element ?: continue
            val channel = element.getAttribute("channel")
            val start = element.getAttribute("start")
            val stop = element.getAttribute("stop")
            val title = firstText(element, "title").ifEmpty { "(untitled)" }
            val description = firstText(element, "desc")

            programs += Program(
                id = "$channel|$start|$title",
                channelId = channel,
                title = title,
                start = start,
                stop = stop,
                description = description,
            )
        }
        return programs
    }

    private fun firstText(parent: org.w3c.dom.Element, tag: String): String {
        val nodes = parent.getElementsByTagName(tag)
        if (nodes.length == 0) return ""
        return nodes.item(0).textContent?.trim().orEmpty()
    }
}
