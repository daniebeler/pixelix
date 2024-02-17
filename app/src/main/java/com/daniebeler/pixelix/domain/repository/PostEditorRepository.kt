package com.daniebeler.pixelix.domain.repository

import android.content.Context
import android.net.Uri
import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.data.remote.dto.CreatePostDto
import com.daniebeler.pixelix.domain.model.MediaAttachment
import com.daniebeler.pixelix.domain.model.MediaAttachmentConfiguration
import com.daniebeler.pixelix.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface PostEditorRepository {
    fun uploadMedia(
        uri: Uri,
        description: String,
        context: Context,
        mediaAttachmentConfiguration: MediaAttachmentConfiguration
    ): Flow<Resource<MediaAttachment>>

    fun updateMedia(id: String, description: String): Flow<Resource<MediaAttachment>>
    fun createPost(createPostDto: CreatePostDto): Flow<Resource<Post>>
    fun deletePost(postId: String): Flow<Resource<Post>>
}