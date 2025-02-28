package com.daniebeler.pfpixelix.domain.repository.serializers

import co.touchlab.kermit.Logger
import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.nodes.Document
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal object HtmlAsTextSerializer : KSerializer<String> {
    override val descriptor = PrimitiveSerialDescriptor("com.daniebeler.HtmlAsTextSerializer", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: String) = encoder.encodeString(value)
    override fun deserialize(decoder: Decoder): String {
        val html = decoder.decodeString()
        val document = Ksoup.parse(html)
        document.outputSettings(Document.OutputSettings().prettyPrint(false)) // Prevent auto formatting
        document.select("br").append("\\n") // Replace <br> with newlines
        document.select("p").prepend("\\n\\n") // Add double newline for paragraphs

        val text = document.text().replace("\\n", "\n")
        val cleanedText = text.lines().joinToString("\n") { it.trimStart() } // Trim leading spaces

        Logger.d { cleanedText }
        return cleanedText.trim()
    }
}

