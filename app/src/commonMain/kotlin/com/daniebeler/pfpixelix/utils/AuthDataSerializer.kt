package com.daniebeler.pfpixelix.utils

import androidx.datastore.core.okio.OkioSerializer
import com.daniebeler.pfpixelix.domain.model.AuthData
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import okio.BufferedSink
import okio.BufferedSource

object AuthDataSerializer: OkioSerializer<AuthData> {
    override val defaultValue: AuthData
        get() = AuthData()

    override suspend fun readFrom(source: BufferedSource): AuthData {
        return try {
            Json.decodeFromString(
                deserializer = AuthData.serializer(),
                string = source.readUtf8()
            )
        } catch (e: SerializationException) {
            e.printStackTrace();
            defaultValue
        }
    }

    override suspend fun writeTo(t: AuthData, sink: BufferedSink) {
        sink.write(
            Json.encodeToString(
                serializer = AuthData.serializer(),
                value = t
            ).encodeToByteArray()
        )
    }
}