
package com.citrus.pixel.viewmodels

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
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
import androidx.compose.ui.graphics.Brush
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

        val gradientColors = listOf(Color(0xFF9C5D64), Color(0xFF522327))
        val gradientBrush = Brush.linearGradient(gradientColors)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = gradientBrush),
            contentAlignment = Alignment.Center
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                MainScreen(viewModel = viewModel)
            }
        }
    }


    private fun loadBitmapFromUri(context: Context, uri: Uri, maxResolution: Int =   2048): Bitmap {
        return try {
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
                BitmapFactory.decodeStream(context.contentResolver.openInputStream(uri), null, this)
                inSampleSize = calculateInSampleSize(this, maxResolution, maxResolution)
                inJustDecodeBounds = false
            }
            BitmapFactory.decodeStream(context.contentResolver.openInputStream(uri), null, options)
                ?: Bitmap.createBitmap(1,   1, Bitmap.Config.ARGB_8888)
        } catch (exception: IOException) {
            Log.e("ConvertActivity", "Error loading bitmap: ${exception.message}")
            Bitmap.createBitmap(1,   1, Bitmap.Config.ARGB_8888)
        }
    }
    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize =  1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height /  2
            val halfWidth = width /  2
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *=  2
            }
        }
        return inSampleSize
    }
}


