package com.citrus.pixel.ui

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp


class ClippedCornerShape(private val clipDp: Int = 16) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val clipAmount: Float = with(density) { clipDp.dp.toPx() }
        val path = Path().apply {
            moveTo(clipAmount, 0f)
            lineTo(size.width - clipAmount, 0f)
            lineTo(size.width, clipAmount)
            lineTo(size.width, size.height - clipAmount)
            lineTo(size.width - clipAmount, size.height)
            lineTo(clipAmount, size.height)
            lineTo(0f, size.height - clipAmount)
            lineTo(0f, clipAmount)
            close()
        }
        return Outline.Generic(path)
    }
}