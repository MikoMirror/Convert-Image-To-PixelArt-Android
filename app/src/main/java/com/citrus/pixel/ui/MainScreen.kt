package com.citrus.pixel.ui

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import com.citrus.pixel.utils.Pixelization

@Composable
fun MainScreen(originalBitmap: Bitmap, pixelization: Pixelization, displayedBitmapState: MutableState<Bitmap>) {
    val currentMode = remember { mutableStateOf(ActionMode.NORMAL) }

    LaunchedEffect(pixelization.sliderValue.value) {
        when (currentMode.value) {
            ActionMode.NORMAL -> displayedBitmapState.value = originalBitmap
            ActionMode.PIXELIZE -> displayedBitmapState.value = pixelization.toPixelizedBitmap(originalBitmap)
            else -> {}  // handle other cases or throw an error
        }
    }

    // States for pinch-to-zoom and panning
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset(0f, 0f)) }
    val state = rememberTransformableState { zoomChange, offsetChange, _ ->
        scale *= zoomChange
        offset = Offset(offset.x + offsetChange.x, offset.y + offsetChange.y)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Image(
            bitmap = displayedBitmapState.value.asImageBitmap(),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offset.x,
                    translationY = offset.y
                )
                .transformable(state = state),  // Apply the transformable modifier
            contentScale = ContentScale.Fit
        )

        CustomNavigationBar(
            currentMode = currentMode,
            onModeChange = { newMode -> currentMode.value = newMode },
            pixelization = pixelization,
            displayedBitmapState = displayedBitmapState,
            originalBitmap = originalBitmap
        )
    }
}

