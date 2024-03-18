package com.daniebeler.pfpixelix.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.daniebeler.pfpixelix.domain.model.SavedSearches
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToStream
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Singleton

@Singleton
class SavedSearchesSerializer: Serializer<SavedSearches> {
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