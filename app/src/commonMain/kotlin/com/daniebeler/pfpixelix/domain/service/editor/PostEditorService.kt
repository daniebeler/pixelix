package com.daniebeler.pfpixelix.domain.service.editor

import com.daniebeler.pfpixelix.domain.model.NewPost
import com.daniebeler.pfpixelix.domain.model.UpdatePost
import com.daniebeler.pfpixelix.domain.repository.PixelfedApi
import com.daniebeler.pfpixelix.domain.service.platform.Platform
import com.daniebeler.pfpixelix.domain.service.utils.loadResource
import com.daniebeler.pfpixelix.utils.KmpUri
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Inject

@Inject
class PostEditorService(
    private val api: PixelfedApi,
    private val platform: Platform,
    private val json: Json
) {

    fun uploadMedia(uri: KmpUri, description: String) = loadResource {
        val file = platform.getPlatformFile(uri) ?: error("File doesn't exist")
        val bytes = file.readBytes()
        val thumbnail = file.getThumbnail()

        val data = MultiPartFormDataContent(
            parts = formData {
                append("description", description)
                append("file", bytes, Headers.build {
                    append(HttpHeaders.ContentType, file.getMimeType())
                    append(HttpHeaders.ContentDisposition, "filename=${file.getName()}")
                })
                if (thumbnail != null) {
                    append("thumbnail", thumbnail, Headers.build {
                        append(HttpHeaders.ContentDisposition, "filename=thumbnail")
                    })
                }
            }
        )

        api.uploadMedia(data)
    }

    fun updateMedia(id: String, description: String) = loadResource {
        api.updateMedia(id, description)
    }

    fun createPost(createPostDto: NewPost) = loadResource {
        api.createPost(json.encodeToString(createPostDto))
    }

    fun updatePost(postId: String, updatePostDto: UpdatePost) = loadResource {
        api.updatePost(postId, json.encodeToString(updatePostDto))
    }

    fun deletePost(postId: String) = loadResource {
        api.deletePost(postId)
    }
}