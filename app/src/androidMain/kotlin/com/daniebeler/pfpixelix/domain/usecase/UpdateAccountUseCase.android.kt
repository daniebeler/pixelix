package com.daniebeler.pfpixelix.domain.usecase

import co.touchlab.kermit.Logger
import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.Account
import com.daniebeler.pfpixelix.domain.repository.AccountRepository
import com.daniebeler.pfpixelix.utils.KmpContext
import com.daniebeler.pfpixelix.utils.KmpUri
import com.daniebeler.pfpixelix.utils.MimeType
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
actual class UpdateAccountUseCase actual constructor(
    private val accountRepository: AccountRepository
) {
    actual operator fun invoke(
        displayName: String,
        note: String,
        website: String,
        privateProfile: Boolean,
        avatarUri: KmpUri?,
        context: KmpContext
    ): Flow<Resource<Account>> {
        val data = MultiPartFormDataContent(formData {
            if (avatarUri != null) {
                try {
                    val fileType = MimeType.getMimeType(avatarUri, context) ?: "image/*"
                    val fileName = "filename=avatar"
                    val bytes = context.contentResolver.openInputStream(avatarUri)?.readBytes()
                    append("avatar", bytes!!, Headers.build {
                        append(HttpHeaders.ContentType, fileType)
                        append(HttpHeaders.ContentDisposition, fileName)
                    })
                } catch (e: Exception) {
                    Logger.e("UpdateAccountUseCase") { e.message!! }
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