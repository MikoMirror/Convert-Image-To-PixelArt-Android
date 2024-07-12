package com.citrus.pixel.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.IOException
import java.io.File

class ExportImage(private val context: Context) {

    suspend fun saveImage(bitmap: Bitmap, fileName: String = "image_${System.currentTimeMillis()}.png"): Result<Uri> {
        return withContext(Dispatchers.IO) {
            try {
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, "${Environment.DIRECTORY_PICTURES}${File.separator}PixelCitrus")
                }

                val resolver = context.contentResolver
                val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                    ?: throw IOException("Failed to create media entry")

                resolver.openOutputStream(uri)?.use { outputStream ->
                    if (!bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)) {
                        throw IOException("Failed to save bitmap")
                    }
                } ?: throw IOException("Failed to open output stream")

                Result.success(uri)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    companion object {
        const val SAVE_LOCATION = "Pictures/PixelCitrus"
    }
}