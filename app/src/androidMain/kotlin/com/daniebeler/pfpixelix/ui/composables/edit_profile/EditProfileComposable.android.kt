package com.daniebeler.pfpixelix.ui.composables.edit_profile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.daniebeler.pfpixelix.utils.KmpUri

@Composable
actual fun rememberAvatarPicker(callback: (KmpUri) -> Unit): () -> Unit {
    val imageCropLauncher = rememberLauncherForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            // use the cropped image
            if (result.uriContent != null) {
                callback(result.uriContent!!)
            }
        } else {
            // an error occurred cropping
            println(result.error)
        }
    }

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                val cropOptions = CropImageContractOptions(
                    uri, CropImageOptions(
                        fixAspectRatio = true,
                        aspectRatioX = 1,
                        aspectRatioY = 1,
                        cropShape = CropImageView.CropShape.OVAL
                    )
                )
                imageCropLauncher.launch(cropOptions)
            }
        })

    return {
        singlePhotoPickerLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }
}