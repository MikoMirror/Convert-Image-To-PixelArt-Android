package com.citrus.pixel.utils

import androidx.compose.runtime.*
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect



class Pixelization {
    var sliderValue by mutableStateOf(0.1f)
        private set
    fun updateSliderValue(newValue: Float) {
        sliderValue = newValue
    }

    fun toPixelizedBitmap(bitmap: Bitmap): Bitmap {
        val scaleFactor = determineScaleFactor()
        val (scaledWidth, scaledHeight) = calculateScaledDimensions(bitmap, scaleFactor)

        return createPixelizedBitmap(bitmap, scaledWidth, scaledHeight)
    }

    private fun determineScaleFactor(): Float = 1 + (sliderValue - 0.1f) * 12

    private fun calculateScaledDimensions(bitmap: Bitmap, scaleFactor: Float): Pair<Int, Int> {
        val scaledWidth = (bitmap.width / scaleFactor).toInt().coerceAtLeast(1)
        val scaledHeight = (bitmap.height / scaleFactor).toInt().coerceAtLeast(1)
        return scaledWidth to scaledHeight
    }

    private fun createPixelizedBitmap(bitmap: Bitmap, scaledWidth: Int, scaledHeight: Int): Bitmap {
        val mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)

        val miniBitmap = Bitmap.createScaledBitmap(mutableBitmap, scaledWidth, scaledHeight, false)
        val outputBitmap = Bitmap.createBitmap(mutableBitmap.width, mutableBitmap.height, Bitmap.Config.ARGB_8888)
        Canvas(outputBitmap).apply {
            val paint = Paint().apply {
                isAntiAlias = false
                isFilterBitmap = false
                isDither = true
            }
            drawBitmap(miniBitmap, null, Rect(0, 0, mutableBitmap.width, mutableBitmap.height), paint)
        }
        return outputBitmap
    }
}