package com.daniebeler.pfpixelix.domain.repository

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.data.remote.dto.CreatePostDto
import com.daniebeler.pfpixelix.data.remote.dto.UpdatePostDto
import com.daniebeler.pfpixelix.domain.model.MediaAttachment
import com.daniebeler.pfpixelix.domain.model.Post
import com.daniebeler.pfpixelix.utils.KmpContext
import com.daniebeler.pfpixelix.utils.KmpUri
import kotlinx.coroutines.flow.Flow

interface PostEditorRepository {
    fun uploadMedia(
        uri: KmpUri,
        description: String,
        context: KmpContext,
    ): Flow<Resource<MediaAttachment>>

    fun updateMedia(id: String, description: String): Flow<Resource<MediaAttachment>>
    fun createPost(createPostDto: CreatePostDto): Flow<Resource<Post>>
    fun updatePost(postId: String, updatePostDto: UpdatePostDto): Flow<Resource<Post?>>
    fun deletePost(postId: String): Flow<Resource<Post>>
}