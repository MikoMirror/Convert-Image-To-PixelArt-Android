package com.citrus.pixel.utils

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private val _sliderValue = MutableLiveData<Float>(0.1f)
    val sliderValue: LiveData<Float> = _sliderValue

    private val _originalBitmap = MutableLiveData<Bitmap>()
    private val _pixelizedBitmap = MutableLiveData<Bitmap>()
    val pixelizedBitmap: LiveData<Bitmap> = _pixelizedBitmap

    fun setOriginalBitmap(bitmap: Bitmap) {
        _originalBitmap.value = bitmap
        updatePixelizedBitmap()
    }

    fun updateSliderValue(newValue: Float) {
        _sliderValue.value = newValue
        updatePixelizedBitmap()
    }

    private fun updatePixelizedBitmap() {
        val originalBitmap = _originalBitmap.value ?: return
        val pixelization = Pixelization()
        pixelization.updateSliderValue(_sliderValue.value ?: 0.1f)
        _pixelizedBitmap.value = pixelization.toPixelizedBitmap(originalBitmap)
    }
}