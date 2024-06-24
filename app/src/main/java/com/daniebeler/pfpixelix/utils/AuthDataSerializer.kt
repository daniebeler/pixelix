package com.daniebeler.pfpixelix.utils

import androidx.datastore.core.Serializer
import com.daniebeler.pfpixelix.domain.model.AuthData
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Singleton

@Singleton
class AuthDataSerializer: Serializer<AuthData> {
    override val defaultValue: AuthData
        get() = AuthData()

    override suspend fun readFrom(input: InputStream): AuthData {
        return try {
            Json.decodeFromString(
                deserializer = AuthData.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            e.printStackTrace();
            defaultValue
        }
    }

    override suspend fun writeTo(t: AuthData, output: OutputStream) {
        output.write(
            Json.encodeToString(
                serializer = AuthData.serializer(),
                value = t
            ).encodeToByteArray()
        )
    }
}