package com.daniebeler.pfpixelix.domain.service.account

import co.touchlab.kermit.Logger
import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.data.remote.PixelfedApi
import com.daniebeler.pfpixelix.domain.model.Account
import com.daniebeler.pfpixelix.domain.service.platform.Platform
import com.daniebeler.pfpixelix.domain.service.session.AuthService
import com.daniebeler.pfpixelix.domain.service.utils.loadListResources
import com.daniebeler.pfpixelix.domain.service.utils.loadResource
import com.daniebeler.pfpixelix.utils.KmpUri
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import me.tatarka.inject.annotations.Inject

@Inject
class AccountService(
    private val authService: AuthService,
    private val api: PixelfedApi,
    private val platform: Platform
) {

    fun getOwnAccount(): Flow<Resource<Account>> {
        val current = authService.getCurrentSession()
        return if (current == null) {
            flowOf(Resource.Error("No account found"))
        } else {
            getAccount(current.accountId)
        }
    }

    fun updateAccount(
        displayName: String,
        note: String,
        website: String,
        privateProfile: Boolean,
        avatarUri: KmpUri?
    ) = loadResource {
        val file = avatarUri?.let { platform.getPlatformFile(avatarUri) }
        val avatarBytes = file?.readBytes()
        val body = MultiPartFormDataContent(formData {
            if (avatarBytes != null) {
                try {
                    val fileName = "filename=avatar"
                    val fileType = file.getMimeType()
                    append("avatar", avatarBytes, Headers.build {
                        append(HttpHeaders.ContentType, fileType)
                        append(HttpHeaders.ContentDisposition, fileName)
                    })
                } catch (e: Exception) {
                    Logger.e("AccountService.updateAccount error", e)
                }
            }

            append("display_name", displayName)
            append("note", note)
            append("website", website)
            append("locked", privateProfile.toString())
        })
        api.updateAccount(body)
    }

    fun getAccount(accountId: String) = loadResource { api.getAccount(accountId) }
    fun getAccountByUsername(username: String) = loadResource { api.getAccountByUsername(username) }
    fun getMutualFollowers(userId: String) = loadListResources { api.getMutalFollowers(userId) }
    fun getAccountSettings() = loadResource { api.getSettings() }
    fun followAccount(accountId: String) = loadResource { api.followAccount(accountId) }
    fun unfollowAccount(accountId: String) = loadResource { api.unfollowAccount(accountId) }
    fun muteAccount(accountId: String) = loadResource { api.muteAccount(accountId) }
    fun unMuteAccount(accountId: String) = loadResource { api.unmuteAccount(accountId) }
    fun blockAccount(accountId: String) = loadResource { api.blockAccount(accountId) }
    fun unblockAccount(accountId: String) = loadResource { api.unblockAccount(accountId) }
    fun getMutedAccounts() = loadListResources { api.getMutedAccounts() }
    fun getBlockedAccounts() = loadListResources { api.getBlockedAccounts() }
    fun getLikedBy(postId: String) = loadListResources { api.getAccountsWhoLikedPost(postId) }

    fun getAccountsFollowers(accountId: String, maxId: String? = null) = loadListResources {
        api.getAccountsFollowers(accountId, maxId)
    }

    fun getAccountsFollowing(accountId: String, maxId: String? = null) = loadListResources {
        api.getAccountsFollowing(accountId, maxId)
    }
}
