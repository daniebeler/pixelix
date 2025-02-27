package com.daniebeler.pfpixelix.data.repository

import co.touchlab.kermit.Logger
import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.data.remote.PixelfedApi
import com.daniebeler.pfpixelix.data.remote.dto.CreatePostDto
import com.daniebeler.pfpixelix.data.remote.dto.MediaAttachmentDto
import com.daniebeler.pfpixelix.data.remote.dto.PostDto
import com.daniebeler.pfpixelix.data.remote.dto.UpdatePostDto
import com.daniebeler.pfpixelix.domain.model.MediaAttachment
import com.daniebeler.pfpixelix.domain.model.Post
import com.daniebeler.pfpixelix.domain.repository.PostEditorRepository
import com.daniebeler.pfpixelix.domain.service.platform.Platform
import com.daniebeler.pfpixelix.utils.KmpContext
import com.daniebeler.pfpixelix.utils.KmpUri
import com.daniebeler.pfpixelix.utils.NetworkCall
import com.daniebeler.pfpixelix.utils.execute
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Inject

class PostEditorRepositoryImpl @Inject constructor(
    private val pixelfedApi: PixelfedApi,
    private val json: Json,
    private val platform: Platform
) : PostEditorRepository {

    override fun uploadMedia(
        uri: KmpUri,
        description: String,
        context: KmpContext,
    ): Flow<Resource<MediaAttachment>> = flow {
        try {
            emit(Resource.Loading())

            val file = platform.getPlatformFile(uri) ?: return@flow
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

            try {
                val res = pixelfedApi.uploadMedia(data).execute().toModel()
                emit(Resource.Success(res))
            } catch (e: Exception) {
                Logger.d(e.message.toString())
                emit(Resource.Error("Unknown Error"))
            }
        } catch (exception: Exception) {
            emit(Resource.Error(exception.message!!))
        }
    }

    override fun updateMedia(id: String, description: String): Flow<Resource<MediaAttachment>> {
        return NetworkCall<MediaAttachment, MediaAttachmentDto>().makeCall(
            pixelfedApi.updateMedia(
                id, description
            )
        )
    }

    override fun createPost(createPostDto: CreatePostDto): Flow<Resource<Post>> = flow {
        try {
            emit(Resource.Loading())
            val res = pixelfedApi.createPost(json.encodeToString(createPostDto)).execute().toModel()
            emit(Resource.Success(res))
        } catch (exception: Exception) {
            if (exception.message != null) {
                emit(Resource.Error(exception.message!!))
            } else {
                emit(Resource.Error("Unknown Error"))
            }
        }
    }

    override fun updatePost(postId: String, updatePostDto: UpdatePostDto): Flow<Resource<Post?>> =
        flow {
            try {
                emit(Resource.Loading())
                pixelfedApi.updatePost(postId, json.encodeToString(updatePostDto)).execute()
                emit(Resource.Success(null))
            } catch (exception: Exception) {
                if (exception.message != null) {
                    emit(Resource.Error(exception.message!!))
                } else {
                    emit(Resource.Error("Unknown Error"))
                }
            }
        }

    override fun deletePost(postId: String): Flow<Resource<Post>> {
        return NetworkCall<Post, PostDto>().makeCall(
            pixelfedApi.deletePost(
                postId
            )
        )
    }
}
