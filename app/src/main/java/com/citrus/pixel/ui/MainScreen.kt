package com.citrus.pixel.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.Alignment
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.toggleScale
import net.engawapg.lib.zoomable.zoomable



@Composable
fun MainScreen(viewModel: MainViewModel) {
    val context = LocalContext.current
    val displayedBitmapState = viewModel.pixelizedBitmap.observeAsState()
    val isNavBarVisible = remember { mutableStateOf(false) }
    val isAnimatingIn = remember { mutableStateOf(false) }

    val navBarHeight = 162.dp
    val animationSpec = tween<Float>(durationMillis = 500)
    val animationProgress by animateFloatAsState(
        targetValue = if (isAnimatingIn.value) 1f else 0f,
        animationSpec = animationSpec,
        label = "NavBarAnimation"
    )

    LaunchedEffect(displayedBitmapState.value) {
        if (displayedBitmapState.value != null) {
            isNavBarVisible.value = true
            val delayDuration = if (areAnimationsEnabled(context)) 150 else 0
            delay(delayDuration.toLong())
            isAnimatingIn.value = true
        }
    }

    val zoomState = rememberZoomState()
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val transformState = rememberTransformableState { zoomChange, offsetChange, _ ->
        scale *= zoomChange
        offset += offsetChange
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = navBarHeight * animationProgress)
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            displayedBitmapState.value?.let { pixelizedBitmap ->
                Image(
                    bitmap = pixelizedBitmap.asImageBitmap(),
                    contentDescription = "Pixelized Image",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .transformable(state = transformState)
                        .zoomable(zoomState = zoomState, onDoubleTap = { position ->
                            zoomState.toggleScale(3f, position)
                        }),
                    contentScale = ContentScale.Fit
                )
            }
        }

        if (isNavBarVisible.value) {
            CustomNavigationBar(
                viewModel = viewModel,
                animationProgress = animationProgress,
                navBarHeight = navBarHeight,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}