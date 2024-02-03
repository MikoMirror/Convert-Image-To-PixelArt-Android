package com.citrus.pixel.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.citrus.pixel.utils.MainViewModel
import com.citrus.pixel.utils.areAnimationsEnabled
import kotlinx.coroutines.delay
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.toggleScale
import net.engawapg.lib.zoomable.zoomable

val TopBarHeight = 56.dp

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val zoomState = rememberZoomState()
    val targetScale = 3f
    val context = LocalContext.current
    val animateNavBarIn = remember { mutableStateOf(false) }
    val displayedBitmapState = viewModel.pixelizedBitmap.observeAsState()
    val isImageLoaded = remember { mutableStateOf(false) }

    LaunchedEffect(displayedBitmapState.value) {
        if (displayedBitmapState.value != null) {
            val delayDuration = if (areAnimationsEnabled(context)) 150 else 0
            delay(delayDuration.toLong())
            isImageLoaded.value = true
            animateNavBarIn.value = true
        }
    }

    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset(0f, 0f)) }
    val transformState = rememberTransformableState { zoomChange, offsetChange, _ ->
        scale *= zoomChange
        offset = Offset(offset.x + offsetChange.x, offset.y + offsetChange.y)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(TopBarHeight))

        displayedBitmapState.value?.let { pixelizedBitmap ->
            Image(
                bitmap = pixelizedBitmap.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .transformable(state = transformState)
                    .zoomable(
                        zoomState = zoomState,
                        onDoubleTap = { position ->
                            zoomState.toggleScale(targetScale, position)
                        }
                    ),
                contentScale = ContentScale.Fit
            )
        }

            CustomNavigationBar(
                viewModel = viewModel,
                isAnimatingIn = animateNavBarIn.value
            )
        }
    }
