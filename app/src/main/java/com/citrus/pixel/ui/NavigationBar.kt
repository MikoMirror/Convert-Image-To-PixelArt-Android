package com.citrus.pixel.ui
import android.content.Context
import android.graphics.Bitmap
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.citrus.pixel.utils.ExportImage
import com.citrus.pixel.utils.MainViewModel


@Composable
fun CustomNavigationBar(
    viewModel: MainViewModel,
    isAnimatingIn: Boolean
) {
    val navBarHeight = 112.dp
    val offsetY by animateDpAsState(
        targetValue = if (isAnimatingIn) 0.dp else navBarHeight,
        animationSpec = tween(durationMillis = 500), label = ""
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(navBarHeight)
            .offset(y = offsetY)
            .background(
                color = Color(0xFF004069),
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        PixelizeControls(
            viewModel = viewModel,
            context = LocalContext.current
        )
    }
}

@Composable
fun PixelizeControls(
    viewModel: MainViewModel,
    context: Context
) {
    val pixelizedBitmap = viewModel.pixelizedBitmap.observeAsState().value

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        val sliderValue = viewModel.sliderValue.observeAsState(0.1f)

        Slider(
            value = sliderValue.value,
            onValueChange = { newValue ->
                viewModel.updateSliderValue(newValue)
            },
            valueRange = 0.1f..1.5f,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            CustomButton(text = "Save", onClick = {
                pixelizedBitmap?.let { bitmap ->
                    ExportImage().saveImage(bitmap, context)
                }
            })
        }
    }
}