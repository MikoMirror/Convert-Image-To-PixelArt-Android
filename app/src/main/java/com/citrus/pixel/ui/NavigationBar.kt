package com.citrus.pixel.ui
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import com.citrus.pixel.utils.ExportImage
import com.citrus.pixel.utils.MainViewModel
import com.citrus.pixel.utils.drawTopBorder
import com.cytrus.pixelarr.R
import kotlinx.coroutines.launch


@Composable
fun CustomNavigationBar(
    viewModel: MainViewModel,
    animationProgress: Float,
    navBarHeight: Dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(navBarHeight)
            .offset(y = (1f - animationProgress) * navBarHeight)
            .background(
                color = Color(0xFFFFF8E3),
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )
            .drawWithContent {
                drawContent()
                drawTopBorder(
                    color = Color.Black,
                    strokeWidth = 2.dp.toPx(),
                    cornerRadius = 16.dp.toPx()
                )
            },
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
    val sliderValue = viewModel.sliderValue.observeAsState(0.1f)
    val coroutineScope = rememberCoroutineScope()
    val exportImage = remember { ExportImage(context) }
    var isSaving by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        sliderRange.value = viewModel.getSliderRangeForImage()
    }

    fun adjustSliderValue(delta: Float) {
        val newValue = (sliderValue.value + delta).coerceIn(
            sliderRange.value.start,
            sliderRange.value.endInclusive
        )
        viewModel.updateSliderValue(newValue)
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SliderNavigationButton(text = "-", onClick = { adjustSliderValue(-0.01f) })

            Text(
                text = "Density: ${"%.2f".format(sliderValue.value)}",
                color = Color.Black,
                fontSize = 20.sp,
                fontFamily = customFontFamily,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )

            SliderNavigationButton(text = "+", onClick = { adjustSliderValue(0.01f) })
        }

        Slider(
            value = sliderValue.value,
            onValueChange = { newValue -> viewModel.updateSliderValue(newValue) },
            valueRange = sliderRange.value,
            colors = SliderDefaults.colors(
                thumbColor = Color.Yellow,
                activeTrackColor = Color.Green,
                inactiveTrackColor = Color.Gray
            ),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),

            contentAlignment = Alignment.Center
        ) {
            CustomButton(
                text = if (isSaving) "Saving..." else "Save",
                onClick = {
                    val bitmap = viewModel.pixelizedBitmap.value
                    if (bitmap != null && !isSaving) {
                        isSaving = true
                        coroutineScope.launch {
                            try {
                                val result = exportImage.saveImage(bitmap)
                                result.fold(
                                    onSuccess = { uri ->
                                        showToast(
                                            context,
                                            "Image saved in ${ExportImage.SAVE_LOCATION}"
                                        )
                                    },
                                    onFailure = { error ->
                                        showToast(context, "Error saving image: ${error.message}")
                                    }
                                )
                            } finally {
                                isSaving = false
                            }
                        }
                    } else if (bitmap == null) {
                        showToast(context, "No image to save")
                    }
                },
            )
        }
    }
}

private fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}




