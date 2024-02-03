package com.citrus.pixel.utils

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
        viewModelScope.launch(Dispatchers.Default) {
            pixelization.updateSliderValue(_sliderValue.value ?: 0.1f)
            val result = pixelization.toPixelizedBitmap(originalBitmap)
            _pixelizedBitmap.postValue(result)
        }
    }
    fun getSliderRangeForImage(): ClosedFloatingPointRange<Float> {
        val bitmap = _originalBitmap.value ?: return 0.1f..1.5f
        return when {
            bitmap.width <= 1280 -> 0.1f..1.5f // HD
            bitmap.width <= 1920 -> 0.1f..2.5f // Full HD
            bitmap.width <= 2560 -> 0.1f..3.5f // 2K
            else -> 0.1f..4.0f // 4K and above
        }
    }
}
