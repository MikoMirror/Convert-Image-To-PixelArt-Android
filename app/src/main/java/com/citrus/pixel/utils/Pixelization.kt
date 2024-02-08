package com.citrus.pixel.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


/**
 * Utility class for pixelizing bitmaps based on a slider value.
 * Adjusts the size of the bitmap to simulate a pixelated effect.
 */
class Pixelization {
    private var sliderValue =   0.1f

    /**
     * Updates the slider value used for pixelization.
     *
     * @param newValue The new slider value to set.
     */
    fun updateSliderValue(newValue: Float) {
        sliderValue = newValue
    }

    /**
     * Asynchronously converts a given bitmap to a pixelized version.
     * Uses the current slider value to determine the pixelation level.
     *
     * @param bitmap The original bitmap to pixelize.
     * @return The pixelized bitmap.
     * @throws PixelizationException If the pixelization process encounters an error.
     */
    suspend fun toPixelizedBitmap(bitmap: Bitmap): Bitmap = withContext(Dispatchers.Default) {
        try {
            val scaleFactor = determineScaleFactor()
            val (scaledWidth, scaledHeight) = calculateScaledDimensions(bitmap, scaleFactor)
            createPixelizedBitmap(bitmap, scaledWidth, scaledHeight)
        } catch (e: Exception) {
            Log.e("Pixelization", "Failed to pixelize bitmap", e)
            throw PixelizationException("Failed to pixelize bitmap: ${e.message}", e)
        }
    }

    /**
     * Calculates the scale factor based on the current slider value.
     *
     * @return The scale factor to apply to the bitmap dimensions.
     */
    private fun determineScaleFactor(): Float =   1 + (sliderValue -   0.1f) *   12

    /**
     * Computes the scaled dimensions for the bitmap based on the scale factor.
     * Ensures the dimensions are at least  1 to avoid division by zero.
     *
     * @param bitmap The original bitmap.
     * @param scaleFactor The factor by which to scale the bitmap dimensions.
     * @return A pair of integers representing the scaled width and height.
     */
    private fun calculateScaledDimensions(bitmap: Bitmap, scaleFactor: Float): Pair<Int, Int> {
        val scaledWidth = (bitmap.width / scaleFactor).toInt().coerceAtLeast(1)
        val scaledHeight = (bitmap.height / scaleFactor).toInt().coerceAtLeast(1)
        return scaledWidth to scaledHeight
    }

    /**
     * Creates a pixelized version of the bitmap by scaling it down and then drawing it back up to its original size.
     * This process simulates the pixelation effect.
     *
     * @param bitmap The original bitmap.
     * @param scaledWidth The width to scale the bitmap to.
     * @param scaledHeight The height to scale the bitmap to.
     * @return The pixelized bitmap.
     */
    private fun createPixelizedBitmap(bitmap: Bitmap, scaledWidth: Int, scaledHeight: Int): Bitmap {
        val mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val miniBitmap = Bitmap.createScaledBitmap(mutableBitmap, scaledWidth, scaledHeight, true)
        val outputBitmap = Bitmap.createBitmap(mutableBitmap.width, mutableBitmap.height, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(outputBitmap)
        val paint = Paint().apply {
            isAntiAlias = false
            isFilterBitmap = false
            isDither = false
        }
        canvas.drawBitmap(miniBitmap, null, Rect(0,   0, mutableBitmap.width, mutableBitmap.height), paint)

        // Recycles the miniBitmap and releases the Canvas resources safely
        miniBitmap.recycleIfNotRecycled()
        canvas.setBitmap(null)

        return outputBitmap
    }

    /**
     * Extension function to safely recycle a bitmap if it hasn't been recycled already.
     */
    private fun Bitmap.recycleIfNotRecycled() {
        if (!this.isRecycled) {
            this.recycle()
        }
    }
}

/**
 * Custom runtime exception for pixelization failures.
 * Provides a way to handle pixelization errors specifically.
 *
 * @property message The detail message.
 * @property cause The cause of the exception.
 */
class PixelizationException(message: String, cause: Throwable?) : RuntimeException(message, cause)