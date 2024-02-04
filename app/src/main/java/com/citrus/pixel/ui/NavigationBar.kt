package com.citrus.pixel.ui
import android.content.Context
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.citrus.pixel.utils.ExportImage
import com.citrus.pixel.utils.MainViewModel
import com.cytrus.pixelarr.R


@Composable
fun CustomNavigationBar(
    viewModel: MainViewModel,
    isAnimatingIn: Boolean
) {
    val offsetY = animateDpAsState(
        targetValue = if (isAnimatingIn) 0.dp else 162.dp,
        animationSpec = tween(durationMillis = 500), label = ""
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(162.dp)
            .offset(y = offsetY.value)
            .background(color = Color(0xFFFFFBD5), shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
        contentAlignment = Alignment.Center
    ) {
        PixelizeControls(viewModel = viewModel, context = LocalContext.current)
    }
}

@Composable
fun PixelizeControls(
    viewModel: MainViewModel,
    context: Context
) {
    val customFontFamily = FontFamily(Font(R.font.pixellari))
    val sliderRange = remember { mutableStateOf(0.1f..1.5f) }

    LaunchedEffect(Unit) {
        sliderRange.value = viewModel.getSliderRangeForImage()
    }

    val pixelizedBitmap = viewModel.pixelizedBitmap.observeAsState().value
    val sliderValue = viewModel.sliderValue.observeAsState(0.1f)

    fun adjustSliderValue(delta: Float) {
        val newValue = (sliderValue.value + delta).coerceIn(sliderRange.value.start, sliderRange.value.endInclusive)
        viewModel.updateSliderValue(newValue)
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            SliderNavigationButton(text = "-", onClick = { adjustSliderValue(-0.01f) })

            Spacer(Modifier.width(8.dp))
            Text(
                text = "Density Level: ${"%.2f".format(sliderValue.value)}",
                color = Color.Black,
                fontSize = 20.sp,
                fontFamily = customFontFamily,

            )

            Spacer(Modifier.width(8.dp))
            SliderNavigationButton(text = "+", onClick = { adjustSliderValue(0.01f) })
        }

        Slider(
            value = sliderValue.value,
            onValueChange = { newValue ->
                viewModel.updateSliderValue(newValue)
            },
            valueRange = sliderRange.value,
            colors = SliderDefaults.colors(
                thumbColor = Color.Yellow,
                activeTrackColor = Color.Green,
                inactiveTrackColor = Color.Gray
            ),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .weight(1f)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp),

            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            CustomButton(text = "Save", onClick = {
                pixelizedBitmap?.let { bitmap ->
                    ExportImage().saveImage(bitmap, context)
                }

            }
            )
        }
    }
}