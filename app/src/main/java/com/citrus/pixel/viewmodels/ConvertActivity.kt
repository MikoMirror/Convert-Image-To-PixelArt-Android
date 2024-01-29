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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import com.citrus.pixel.ui.MainScreen
import com.citrus.pixel.utils.Pixelization
import java.io.IOException

class ConvertActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ConvertImageScreen(intent.getStringExtra("imageUri"))
        }
        val colorInt = Color(0xFF004069).toArgb()
        window.navigationBarColor = colorInt
    }

    @Composable
    fun ConvertImageScreen(imageUri: String?) {
        val context = LocalContext.current
        val originalBitmap = imageUri?.let {
            Uri.parse(it).toBitmap(context)
        } ?: Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)

        val currentBitmap = rememberSaveable { mutableStateOf(originalBitmap) }
        val displayedBitmapState = rememberSaveable { mutableStateOf(originalBitmap) }
        val pixelization = remember { Pixelization() }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF001929)),
            contentAlignment = Alignment.Center
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                MainScreen(currentBitmap.value, pixelization, displayedBitmapState)
            }
        }
    }
}
private fun Uri.toBitmap(context: Context): Bitmap {
    return try {
        MediaStore.Images.Media.getBitmap(context.contentResolver, this)
    } catch (exception: IOException) {
        Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
    }
}


