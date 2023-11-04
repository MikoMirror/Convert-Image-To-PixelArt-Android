@file:Suppress("DEPRECATION")

package com.citrus.pixel.viewmodels

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.citrus.pixel.ui.MainScreen
import com.citrus.pixel.ui.TopBar
import com.citrus.pixel.utils.Pixelization
import java.io.IOException

class ConvertActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ConvertImageScreen(intent.getStringExtra("imageUri"))
        }
    }

    @Composable
    fun ConvertImageScreen(imageUri: String?) {
        val context = LocalContext.current
        val originalBitmap = imageUri?.let {
            Uri.parse(it).toBitmap(context)
        } ?: Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888) // Fallback to an empty bitmap

        val currentBitmap = rememberSaveable { mutableStateOf(originalBitmap) }
        val displayedBitmapState = rememberSaveable { mutableStateOf(originalBitmap) }
        val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
        val pixelization = remember { Pixelization() }

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(modifier = Modifier.fillMaxSize()) {
                TopBar(
                    onBackClick = {
                        onBackPressedDispatcher?.onBackPressed()
                    },
                    onSettingsClick = {}
                )
                MainScreen(currentBitmap.value, pixelization, displayedBitmapState)
            }
        }
    }
}

private fun Uri.toBitmap(context: Context): Bitmap {
    return try {
        MediaStore.Images.Media.getBitmap(context.contentResolver, this)
    } catch (exception: IOException) {
        Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888) // Return an empty bitmap as a fallback
    }
}


