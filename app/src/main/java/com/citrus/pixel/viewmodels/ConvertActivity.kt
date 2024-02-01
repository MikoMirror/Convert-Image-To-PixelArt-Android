@file:Suppress("DEPRECATION")

package com.citrus.pixel.viewmodels

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.citrus.pixel.ui.MainScreen
import com.citrus.pixel.utils.MainViewModel
import java.io.IOException

class ConvertActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val imageUri = intent.getStringExtra("imageUri")

        setContent {
            ConvertImageScreen(imageUri)
        }

        val colorInt = android.graphics.Color.parseColor("#FFFFFBD5")
        window.navigationBarColor = colorInt
    }

    @Composable
    fun ConvertImageScreen(imageUri: String?) {
        val viewModel: MainViewModel = viewModel()
        val context = LocalContext.current
        val originalBitmap = remember(imageUri) {
            imageUri?.let {
                loadBitmapFromUri(context, Uri.parse(it))
            } ?: Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        }
        LaunchedEffect(originalBitmap) {
            viewModel.setOriginalBitmap(originalBitmap)
        }


        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF7A444A)),
            contentAlignment = Alignment.Center
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                MainScreen(viewModel = viewModel)
            }
        }
    }


    private fun loadBitmapFromUri(context: Context, uri: Uri): Bitmap {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            } else {
                @Suppress("DEPRECATION")
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            }
        } catch (exception: IOException) {
            Log.e("ConvertActivity", "Error loading bitmap: ${exception.message}")
            Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        }
    }
}


