package com.daniebeler.pfpixelix.data.repository

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.data.remote.PixelfedApi
import com.daniebeler.pfpixelix.data.remote.dto.PostDto
import com.daniebeler.pfpixelix.domain.model.LikedPostsWithNext
import com.daniebeler.pfpixelix.domain.model.Post
import com.daniebeler.pfpixelix.domain.repository.PostRepository
import com.daniebeler.pfpixelix.utils.NetworkCall
import com.daniebeler.pfpixelix.utils.executeWithResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val pixelfedApi: PixelfedApi
) : PostRepository {
    override fun getPostById(postId: String): Flow<Resource<Post>> {
        return NetworkCall<Post, PostDto>().makeCall(pixelfedApi.getPostById(postId))
    }

    override fun getPostsByAccountId(
        accountId: String, maxPostId: String, limit: Int
    ): Flow<Resource<List<Post>>> {
        return if (maxPostId.isEmpty()) {
            NetworkCall<Post, PostDto>().makeCallList(
                pixelfedApi.getPostsByAccountId(
                    accountId, limit
                )
            )
        } else {
            NetworkCall<Post, PostDto>().makeCallList(
                pixelfedApi.getPostsByAccountId(
                    accountId, maxPostId, limit
                )
            )
        }
    }

    override fun likePost(postId: String): Flow<Resource<Post>> {
        return NetworkCall<Post, PostDto>().makeCall(pixelfedApi.likePost(postId))
    }

    override fun unlikePost(postId: String): Flow<Resource<Post>> {
        return NetworkCall<Post, PostDto>().makeCall(pixelfedApi.unlikePost(postId))
    }

    override fun reblogPost(postId: String): Flow<Resource<Post>> {
        return NetworkCall<Post, PostDto>().makeCall(pixelfedApi.reblogPost(postId))
    }

    override fun unreblogPost(postId: String): Flow<Resource<Post>> {
        return NetworkCall<Post, PostDto>().makeCall(pixelfedApi.unreblogPost(postId))
    }

    override fun bookmarkPost(postId: String): Flow<Resource<Post>> {
        return NetworkCall<Post, PostDto>().makeCall(pixelfedApi.bookmarkPost(postId))
    }

    override fun unBookmarkPost(postId: String): Flow<Resource<Post>> {
        return NetworkCall<Post, PostDto>().makeCall(
            pixelfedApi.unbookmarkPost(
                postId
            )
        )
    }

    override fun getLikedPosts(maxId: String): Flow<Resource<LikedPostsWithNext>> = flow {
        try {
            emit(Resource.Loading())
            val (response, data) = if (maxId.isNotBlank()) {
                pixelfedApi.getLikedPosts(maxId).executeWithResponse()
            } else {
                pixelfedApi.getLikedPosts().executeWithResponse()
            }


                val linkHeader = response.headers["link"] ?: ""

                val onlyLink =
                    linkHeader.substringAfter("rel=\"next\",<", "").substringBefore(">", "")

                val nextMinId = onlyLink.substringAfter("min_id=", "")

                val res = data.map { it.toModel() }

                val result = LikedPostsWithNext(res, nextMinId)
                emit(Resource.Success(result))
        } catch (exception: Exception) {
            emit(Resource.Error(exception.message ?: "Unknown Error"))
        }
    }

    override fun getBookmarkedPosts(): Flow<Resource<List<Post>>> {
        return NetworkCall<Post, PostDto>().makeCallList(pixelfedApi.getBookmarkedPosts())
    }

    override fun getTrendingPosts(range: String): Flow<Resource<List<Post>>> {
        return NetworkCall<Post, PostDto>().makeCallList(pixelfedApi.getTrendingPosts(range))
    }
}