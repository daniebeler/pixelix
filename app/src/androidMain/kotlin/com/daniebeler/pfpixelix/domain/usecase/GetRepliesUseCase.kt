package com.daniebeler.pfpixelix.domain.usecase

import co.touchlab.kermit.Logger
import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.PostContext
import com.daniebeler.pfpixelix.domain.repository.CountryRepository
import com.fleeksoft.ksoup.Ksoup
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import me.tatarka.inject.annotations.Inject

@Inject
class GetRepliesUseCase(
    private val repository: CountryRepository
) {
    operator fun invoke(postId: String): Flow<Resource<PostContext>> = flow{
        repository.getReplies(postId).collect { result ->
            if (result is Resource.Success && result.data != null) {
                var postContext: PostContext = result.data
                postContext = postContext.copy(descendants = postContext.descendants.map {reply ->
                    val updatedReply = reply.copy(content = htmlToText(reply.content))
                    updatedReply
                })
                emit(Resource.Success(postContext))
            } else {
                emit(result)
            }
        }
    }

    private fun htmlToText(html: String): String {
        val text = Ksoup.parse(html).text()
        Logger.d("htmlToText") { text }
        return text
    }
}