package com.daniebeler.pfpixelix.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.data.remote.PixelfedApi
import com.daniebeler.pfpixelix.data.remote.dto.CreatePostDto
import com.daniebeler.pfpixelix.data.remote.dto.MediaAttachmentDto
import com.daniebeler.pfpixelix.data.remote.dto.PostDto
import com.daniebeler.pfpixelix.data.remote.dto.UpdatePostDto
import com.daniebeler.pfpixelix.domain.model.MediaAttachment
import com.daniebeler.pfpixelix.domain.model.Post
import com.daniebeler.pfpixelix.domain.repository.PostEditorRepository
import com.daniebeler.pfpixelix.utils.GetFile
import com.daniebeler.pfpixelix.utils.MimeType
import com.daniebeler.pfpixelix.utils.NetworkCall
import com.daniebeler.pfpixelix.utils.execute
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import me.tatarka.inject.annotations.Inject

class PostEditorRepositoryImpl @Inject constructor(
    private val pixelfedApi: PixelfedApi
) : PostEditorRepository {


    @SuppressLint("Recycle")
    override fun uploadMedia(
        uri: Uri,
        description: String,
        context: Context,
    ): Flow<Resource<MediaAttachment>> = flow {
        try {
            emit(Resource.Loading())

            //val pixelfedApi = buildPixelFedApi(true)

            val fileType = MimeType.getMimeType(uri, context.contentResolver) ?: "image/*"
            val bytes = context.contentResolver.openInputStream(uri)?.readBytes()
            val file = GetFile.getFile(uri, context) ?: return@flow

            val thumbnailBitmap = if (fileType.take(5) != "image" || fileType == "image/gif") {
                getThumbnail(uri, context)
            } else null

            val data = MultiPartFormDataContent(formData {
                append("file", bytes!!, Headers.build {
                    append(HttpHeaders.ContentType, fileType)
                    append(HttpHeaders.ContentDisposition, file.name)
                })
                if (thumbnailBitmap != null) {
                    bitmapToBytes(thumbnailBitmap)?.let {
                        append("thumbnail", it, Headers.build {
                            append(HttpHeaders.ContentDisposition, "thumbnail")
                        })
                    }
                }
            })

            try {
                val res = pixelfedApi.uploadMedia(data).execute().toModel()
                emit(Resource.Success(res))
            } catch (e: Exception) {
                emit(Resource.Error("Unknown Error"))
            }
        } catch (exception: Exception) {
            emit(Resource.Error(exception.message!!))
        }
    }

    private fun bitmapToBytes(photo: Bitmap): ByteArray? {
        val stream = ByteArrayOutputStream()
        photo.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    private suspend fun getThumbnail(uri: Uri, context: Context): Bitmap? {
        return try {
            withContext(Dispatchers.IO) {
                Glide.with(context).asBitmap().load(uri).apply(RequestOptions().frame(0)).submit()
                    .get()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
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
            val res = pixelfedApi.createPost(createPostDto).execute().toModel()
            emit(Resource.Success(res))
        } catch (exception: Exception) {
            if (exception.message != null) {
                emit(Resource.Error(exception.message!!))
            } else {
                emit(Resource.Error("Unknown Error"))
            }
        }
    }

    override fun updatePost(postId: String, updatePostDto: UpdatePostDto): Flow<Resource<Post?>> = flow {
        try {
            emit(Resource.Loading())
            pixelfedApi.updatePost(postId, updatePostDto).execute()
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
            pixelfedApi.deletePost(postId
            )
        )
    }
}