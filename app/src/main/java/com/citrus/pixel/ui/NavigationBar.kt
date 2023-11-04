package com.citrus.pixel.ui
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
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.citrus.pixel.utils.Pixelization
import com.cytrus.pixelarr.R


enum class ActionMode {
    NORMAL, PIXELIZE, GRID
}

@Composable
fun CustomNavigationBar(
    currentMode: State<ActionMode>,
    onModeChange: (ActionMode) -> Unit,
    pixelization: Pixelization,
    displayedBitmapState: MutableState<Bitmap>,
    originalBitmap: Bitmap
) {
    val navBarHeight = when (currentMode.value) {
        ActionMode.NORMAL, ActionMode.GRID -> 56.dp
        ActionMode.PIXELIZE -> 112.dp
    }

    val animatedHeight by animateDpAsState(targetValue = navBarHeight)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(animatedHeight)
            .background(
                color = Color(0xFF3A90C7),
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        when (currentMode.value) {
            ActionMode.NORMAL -> NormalModeIcons(onModeChange)
            ActionMode.PIXELIZE -> PixelizeSlider(onModeChange, pixelization, displayedBitmapState, originalBitmap)
            ActionMode.GRID -> GridModeIcons(onModeChange)
        }
    }
}

@Composable
fun NormalModeIcons(onModeChange: (ActionMode) -> Unit) {
    val actions = listOf(
        Pair(R.drawable.convert) { onModeChange(ActionMode.PIXELIZE) },
        Pair(R.drawable.draw) {},
        Pair(R.drawable.save) {},
        Pair(R.drawable.grid_on) { onModeChange(ActionMode.GRID) }
    )
    ActionRow(actions)
}

@Composable
fun GridModeIcons(onModeChange: (ActionMode) -> Unit) {
    val backAction = listOf(
        Pair(R.drawable.back) { onModeChange(ActionMode.NORMAL) }
    )
    ActionRow(backAction)
}

@Composable
fun ActionRow(actions: List<Pair<Int, () -> Unit>>) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        actions.forEach { (iconRes, action) ->
            val icon = painterResource(id = iconRes)
            IconButton(onClick = action) {
                Icon(painter = icon, contentDescription = null)
            }
        }
    }
}

@Composable
fun PixelizeSlider(
    onModeChange: (ActionMode) -> Unit,
    pixelization: Pixelization,
    displayedBitmapState: MutableState<Bitmap>,
    originalBitmap: Bitmap
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        pixelization.PixelizationControl()
        Button(onClick = {
            val pixelizedImage = pixelization.toPixelizedBitmap(originalBitmap)
            displayedBitmapState.value = pixelizedImage
            onModeChange(ActionMode.NORMAL)
        }) {
            Text("Apply")
        }
    }
}
