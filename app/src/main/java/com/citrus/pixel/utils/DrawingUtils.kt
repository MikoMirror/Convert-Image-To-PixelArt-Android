package com.citrus.pixel.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

fun DrawScope.drawTopBorder(color: Color, strokeWidth: Float, cornerRadius: Float) {
    val path = Path().apply {
        moveTo(0f, cornerRadius)
        quadraticBezierTo(0f, 0f, cornerRadius, 0f)
        lineTo(size.width - cornerRadius, 0f)
        quadraticBezierTo(size.width, 0f, size.width, cornerRadius)
    }
    drawPath(
        path = path,
        color = color,
        style = Stroke(width = strokeWidth)
    )
}