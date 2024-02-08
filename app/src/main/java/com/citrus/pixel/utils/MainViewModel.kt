package com.citrus.pixel.utils

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


/**
 * ViewModel for pixelizing images based on a slider value.
 * Exposes LiveData for slider value and pixelized bitmap.
 */
class MainViewModel : ViewModel() {
    private val _sliderValue = MutableLiveData<Float>(0.1f)
    val sliderValue: LiveData<Float> = _sliderValue
    private val _originalBitmap = MutableLiveData<Bitmap>()
    private val _pixelizedBitmap = MutableLiveData<Bitmap>()
    val pixelizedBitmap: LiveData<Bitmap> = _pixelizedBitmap

    // Job for debouncing pixelization updates
    private var debounceJob: Job? = null
    private val debounceDurationMillis =   200L

    init {
        // Collect latest slider value and apply debounce
        viewModelScope.launch {
            _sliderValue.asFlow().collectLatest { value ->
                debouncePixelizationUpdate(value)
            }
        }
    }

    /**
     * Debounces pixelization updates to prevent excessive processing.
     *
     * @param newValue New slider value to apply after debounce.
     */
    private fun debouncePixelizationUpdate(newValue: Float) {
        debounceJob?.cancel()
        debounceJob = viewModelScope.launch(Dispatchers.IO) {
            delay(debounceDurationMillis)
            updatePixelizedBitmap(newValue)
        }
    }

    /**
     * Sets the original bitmap for pixelization.
     *
     * @param bitmap Original bitmap to store.
     */
    fun setOriginalBitmap(bitmap: Bitmap) {
        _originalBitmap.value = bitmap
    }

    /**
     * Updates the slider value and initiates pixelization.
     *
     * @param newValue Updated slider value.
     */
    fun updateSliderValue(newValue: Float) {
        _sliderValue.value = newValue
    }

    /**
     * Performs pixelization asynchronously based on the slider value.
     *
     * @param newValue Slider value to use for pixelization.
     */
    private fun updatePixelizedBitmap(newValue: Float) {
        val originalBitmap = _originalBitmap.value ?: return
        val pixelization = Pixelization()
        viewModelScope.launch(Dispatchers.IO) {
            pixelization.updateSliderValue(newValue)
            val result = pixelization.toPixelizedBitmap(originalBitmap)
            _pixelizedBitmap.postValue(result)
        }
    }

    /**
     * Calculates the slider range based on the original bitmap's width.
     *
     * @return Range of valid slider values for the current image.
     */
    fun getSliderRangeForImage(): ClosedFloatingPointRange<Float> {
        val bitmap = _originalBitmap.value ?: return   0.1f..1.5f
        return when {
            bitmap.width <=   1280 ->   0.1f..1.5f // HD
            bitmap.width <=   1920 ->   0.1f..2.5f // Full HD
            bitmap.width <=   2560 ->   0.1f..3.5f //   2K
            else ->   0.1f..4.5f //   4K and above
        }
    }
}