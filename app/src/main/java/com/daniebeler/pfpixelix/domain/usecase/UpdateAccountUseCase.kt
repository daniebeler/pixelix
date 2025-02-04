package com.daniebeler.pfpixelix.domain.usecase

import android.content.Context
import android.net.Uri
import android.util.Log
import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.Account
import com.daniebeler.pfpixelix.domain.repository.AccountRepository
import com.daniebeler.pfpixelix.utils.MimeType
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.flow.Flow


class UpdateAccountUseCase(
    private val accountRepository: AccountRepository
) {
    operator fun invoke(
        displayName: String, note: String, website: String, privateProfile: Boolean, avatarUri: Uri?, context: Context
    ): Flow<Resource<Account>> {
        val data = MultiPartFormDataContent(formData {
            if (avatarUri != null) {
                try {
                    val fileType = MimeType.getMimeType(avatarUri, context.contentResolver) ?: "image/*"
                    val fileName = "filename=avatar"
                    val bytes = context.contentResolver.openInputStream(avatarUri)?.readBytes()
                    append("avatar", bytes!!, Headers.build {
                        append(HttpHeaders.ContentType, fileType)
                        append(HttpHeaders.ContentDisposition, fileName)
                    })
                } catch (e: Exception) {
                    Log.e("UpdateAccountUseCase", e.message!!)
                }
            }

            append("display_name", displayName)
            append("note", note)
            append("website", website)
            append("locked", privateProfile.toString())
        })

        return accountRepository.updateAccount(data)
    }
}

