package com.citrus.pixel.ui
import android.content.Context
import android.graphics.Bitmap
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.citrus.pixel.utils.ExportImage
import com.citrus.pixel.utils.Pixelization


@Composable
fun CustomNavigationBar(
    pixelization: Pixelization,
    displayedBitmapState: MutableState<Bitmap>,
    originalBitmap: Bitmap,
    context: Context
) {
    val navBarHeight = 112.dp
    val animatedHeight by animateDpAsState(targetValue = navBarHeight)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(animatedHeight)
            .background(
                color = Color(0xFF004069),
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        PixelizeControls(
            pixelization = pixelization,
            displayedBitmapState = displayedBitmapState,
            originalBitmap = originalBitmap,
            context = context
        )
    }
}

@Composable
fun PixelizeControls(
    pixelization: Pixelization,
    displayedBitmapState: MutableState<Bitmap>,
    originalBitmap: Bitmap,
    context: Context
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        pixelization.PixelizationControl()

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            CustomButton(text = "Save", onClick = {
                ExportImage().saveImage(displayedBitmapState.value, context)
            })
        }
    }
}