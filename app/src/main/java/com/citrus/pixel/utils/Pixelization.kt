package com.citrus.pixel.utils

import androidx.compose.runtime.*
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Slider


class Pixelization {
    private val _sliderValue = mutableFloatStateOf(0.1f)
    val sliderValue: State<Float> get() = _sliderValue
    @Composable
    fun PixelizationControl() {
        Column {
            Slider(
                value = _sliderValue.value,
                onValueChange = { newValue -> _sliderValue.value = newValue },
                valueRange = 0.1f..1f
            )
        }
    }

    fun toPixelizedBitmap(bitmap: Bitmap): Bitmap {
        val scaleFactor = determineScaleFactor()
        val (scaledWidth, scaledHeight) = calculateScaledDimensions(bitmap, scaleFactor)

        return createPixelizedBitmap(bitmap, scaledWidth, scaledHeight)
    }

    private fun determineScaleFactor(): Float = 1 + (_sliderValue.value - 0.1f) * 12

    private fun calculateScaledDimensions(bitmap: Bitmap, scaleFactor: Float): Pair<Int, Int> {
        val scaledWidth = (bitmap.width / scaleFactor).toInt().coerceAtLeast(1)
        val scaledHeight = (bitmap.height / scaleFactor).toInt().coerceAtLeast(1)
        return Pair(scaledWidth, scaledHeight)
    }

    private fun createPixelizedBitmap(bitmap: Bitmap, scaledWidth: Int, scaledHeight: Int): Bitmap {
        val miniBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, false)
        val outputBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)
        val canvas = Canvas(outputBitmap)
        val paint = Paint().apply {
            isAntiAlias = false
            isFilterBitmap = false
            isDither = true
        }
        canvas.drawBitmap(miniBitmap, null, Rect(0, 0, bitmap.width, bitmap.height), paint)
        return outputBitmap
    }
}