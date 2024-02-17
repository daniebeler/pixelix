package com.daniebeler.pixelix.domain.repository

import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.domain.model.LikedPostsWithNext
import com.daniebeler.pixelix.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {

    fun getPostsByAccountId(accountId: String, maxPostId: String = ""): Flow<Resource<List<Post>>>
    fun getPostById(postId: String): Flow<Resource<Post>>
    fun likePost(postId: String): Flow<Resource<Post>>
    fun unlikePost(postId: String): Flow<Resource<Post>>
    fun bookmarkPost(postId: String): Flow<Resource<Post>>
    fun unBookmarkPost(postId: String): Flow<Resource<Post>>
    fun getLikedPosts(maxId: String = ""): Flow<Resource<LikedPostsWithNext>>
    fun getBookmarkedPosts(): Flow<Resource<List<Post>>>
    fun getTrendingPosts(range: String): Flow<Resource<List<Post>>>
}