package com.example.md3.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.ext.SdkExtensions
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContract
private const val MIME_TYPE_IMAGE = "image/*"

class PickMultiplePhotosContract : ActivityResultContract<Unit, List<Uri>>() {

    override fun createIntent(context: Context, input: Unit): Intent {

        return if (PhotoPickerAvailabilityChecker.isPhotoPickerAvailable()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && SdkExtensions.getExtensionVersion(
                    Build.VERSION_CODES.R) >= 2) {
                Intent(MediaStore.ACTION_PICK_IMAGES)
                    .putExtra(MediaStore.EXTRA_PICK_IMAGES_MAX, MediaStore.getPickImagesMaxLimit())
            } else {
                TODO("SdkExtensions.getExtensionVersion(R) < 2")
            }
        } else {
            Intent(Intent.ACTION_OPEN_DOCUMENT)
                .putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }.apply { type = MIME_TYPE_IMAGE }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): List<Uri> {
        return if (resultCode == Activity.RESULT_OK) {
            val selectedUris: LinkedHashSet<Uri> = LinkedHashSet()

            if (intent?.clipData != null) {
                val clipData = intent.clipData
                val itemCount = clipData!!.itemCount.coerceAtMost(5) // Limit the itemCount to 4

                for (index in 0 until itemCount) {
                    val uri: Uri? = clipData.getItemAt(index).uri
                    if (uri != null) {
                        selectedUris.add(uri)
                    }
                }
            } else if (intent?.data != null) {
                val uri: Uri? = intent.data
                if (uri != null) {
                    selectedUris.add(uri)
                }
            }

            ArrayList(selectedUris)
        } else {
            emptyList()
        }
    }


}