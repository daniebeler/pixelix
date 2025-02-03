package com.daniebeler.pfpixelix.utils

import co.touchlab.kermit.Logger
import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.nodes.Document

object HtmlToText {
    fun htmlToText(html: String): String {
        val document = Ksoup.parse(html)
        document.outputSettings(Document.OutputSettings().prettyPrint(false)) // Prevent auto formatting
        document.select("br").append("\\n") // Replace <br> with newlines
        document.select("p").prepend("\\n\\n") // Add double newline for paragraphs

        val text = document.text().replace("\\n", "\n")
        val cleanedText = text.lines().joinToString("\n") { it.trimStart() } // Trim leading spaces

        Logger.d("htmlToText") { cleanedText }
        return cleanedText.trim()
    }

}