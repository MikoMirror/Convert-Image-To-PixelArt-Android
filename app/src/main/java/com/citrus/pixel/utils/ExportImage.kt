package com.citrus.pixel.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import java.io.File

class ExportImage {

    fun saveImage(bitmap: Bitmap, context: Context) {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "image_${System.currentTimeMillis()}.png")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + "PixelCitrus")
        }

        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        try {
            uri?.let {
                resolver.openOutputStream(it).use { out ->
                    if (out != null) {
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                        showToast(context, "Image saved in Pictures/PixelCitrus")
                    } else {
                        showToast(context, "Error saving image: Failed to open output stream")
                    }
                }
            } ?: showToast(context, "Error saving image: Failed to create media entry")
        } catch (e: Exception) {
            showToast(context, "Error saving image: ${e.message}")
            e.printStackTrace()
        }
    }

    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}