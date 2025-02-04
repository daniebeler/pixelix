package com.daniebeler.pfpixelix.utils

import androidx.datastore.core.Serializer
import com.daniebeler.pfpixelix.domain.model.SavedSearches
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object SavedSearchesSerializer: Serializer<SavedSearches> {
    override val defaultValue: SavedSearches
        get() = SavedSearches()

    override suspend fun readFrom(input: InputStream): SavedSearches {
        return try {
            Json.decodeFromString(
                deserializer = SavedSearches.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            e.printStackTrace();
            defaultValue
        }
    }

    override suspend fun writeTo(t: SavedSearches, output: OutputStream) {
        output.write(
            Json.encodeToString(
                serializer = SavedSearches.serializer(),
                value = t
            ).encodeToByteArray()
        )
    }
}