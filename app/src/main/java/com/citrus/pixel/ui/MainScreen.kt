package com.citrus.pixel.ui

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.citrus.pixel.utils.Pixelization

val TopBarHeight = 56.dp
val NavBarHeight = 112.dp

@Composable
fun MainScreen(originalBitmap: Bitmap, pixelization: Pixelization, displayedBitmapState: MutableState<Bitmap>) {
    val context = LocalContext.current

    LaunchedEffect(pixelization.sliderValue) {
        displayedBitmapState.value = pixelization.toPixelizedBitmap(originalBitmap)
    }

    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset(0f, 0f)) }
    val state = rememberTransformableState { zoomChange, offsetChange, _ ->
        scale *= zoomChange
        offset = Offset(offset.x + offsetChange.x, offset.y + offsetChange.y)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(TopBarHeight))

        Image(
            bitmap = displayedBitmapState.value.asImageBitmap(),
            contentDescription = null,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offset.x,
                    translationY = offset.y
                )
                .transformable(state = state),
            contentScale = ContentScale.Fit
        )

        CustomNavigationBar(
            pixelization = pixelization,
            displayedBitmapState = displayedBitmapState,
            originalBitmap = originalBitmap,
            context = context
        )
    }
}