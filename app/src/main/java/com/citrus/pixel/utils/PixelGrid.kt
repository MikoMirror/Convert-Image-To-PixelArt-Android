package com.citrus.pixel.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

class PixelGrid(private val bitmap: Bitmap, private val blockSize: Int) {
    fun addGrid(): Bitmap {
        val outputBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)
        val canvas = Canvas(outputBitmap)
        canvas.drawBitmap(bitmap, 0f, 0f, null)

        val paint = Paint().apply {
            color = Color.BLACK
            style = Paint.Style.STROKE
            strokeWidth = 2f
        }

        for (x in 0..bitmap.width step blockSize) {
            canvas.drawLine(x.toFloat(), 0f, x.toFloat(), bitmap.height.toFloat(), paint)
        }

        for (y in 0..bitmap.height step blockSize) {
            canvas.drawLine(0f, y.toFloat(), bitmap.width.toFloat(), y.toFloat(), paint)
        }

        return outputBitmap
    }
}