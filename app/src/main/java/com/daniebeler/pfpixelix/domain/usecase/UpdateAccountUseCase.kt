package com.daniebeler.pfpixelix.domain.usecase

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.data.remote.dto.UpdateAccountDto
import com.daniebeler.pfpixelix.domain.model.Account
import com.daniebeler.pfpixelix.domain.repository.AccountRepository
import com.daniebeler.pfpixelix.utils.GetFile
import com.daniebeler.pfpixelix.utils.MimeType
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody


class UpdateAccountUseCase(
    private val accountRepository: AccountRepository
) {
    operator fun invoke(
        displayName: String, note: String, website: String, privateProfile: Boolean, avatarUri: Uri?, context: Context
    ): Flow<Resource<Account>> {
        val builder: MultipartBody.Builder = MultipartBody.Builder().setType(MultipartBody.FORM)

        if (avatarUri != null) {
            val fileType = MimeType.getMimeType(avatarUri, context.contentResolver) ?: "image/*"

            val inputStream = context.contentResolver.openInputStream(avatarUri)
            val fileRequestBody = inputStream?.readBytes()?.toRequestBody(fileType.toMediaTypeOrNull())

            builder.addFormDataPart("avatar", "avatar", fileRequestBody!!)
        }

        builder.addFormDataPart("display_name", displayName)
        builder.addFormDataPart("note", note)
        builder.addFormDataPart("website", website)
        builder.addFormDataPart("locked", privateProfile.toString())

        val requestBody: RequestBody = builder.build()

        return accountRepository.updateAccount(requestBody)
    }
}

