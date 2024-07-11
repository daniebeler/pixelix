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
import com.daniebeler.pfpixelix.domain.model.MediaAttachmentConfiguration
import com.daniebeler.pfpixelix.domain.model.Post
import com.daniebeler.pfpixelix.domain.repository.PostEditorRepository
import com.daniebeler.pfpixelix.utils.GetFile
import com.daniebeler.pfpixelix.utils.MimeType
import com.daniebeler.pfpixelix.utils.NetworkCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.awaitResponse
import java.io.ByteArrayOutputStream
import javax.inject.Inject

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

            val inputStream = context.contentResolver.openInputStream(uri)
            val fileRequestBody =
                inputStream?.readBytes()?.toRequestBody(fileType.toMediaTypeOrNull())

            val file = GetFile.getFile(uri, context) ?: return@flow

            val builder: MultipartBody.Builder = MultipartBody.Builder().setType(MultipartBody.FORM)
            builder.addFormDataPart("description", description)
                .addFormDataPart("file", file.name, fileRequestBody!!)

            if (fileType.take(5) != "image" || fileType == "image/gif") {
                val thumbnailBitmap = getThumbnail(uri, context)
                if (thumbnailBitmap != null) {
                    bitmapToBytes(thumbnailBitmap)?.let {
                        builder.addFormDataPart(
                            "thumbnail", "thumbnail", it.toRequestBody()
                        )
                    }
                }
            }

            val requestBody: RequestBody = builder.build()
            val response = pixelfedApi.uploadMedia(requestBody
            ).awaitResponse()
            if (response.isSuccessful) {
                val res = response.body()!!.toModel()
                emit(Resource.Success(res))
            } else {
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
            val response = pixelfedApi.createPost(createPostDto)
            if (response != null) {
                val res = response.body()!!.toModel()
                emit(Resource.Success(res))
            } else {
                emit(Resource.Error("Unknown Error"))
            }
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
            val response = pixelfedApi.updatePost(postId, updatePostDto)
            if (response.code() == 200) {
                emit(Resource.Success(null))
            } else {
                emit(Resource.Error("Unknown Error"))
            }
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