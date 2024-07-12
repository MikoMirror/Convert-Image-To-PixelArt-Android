package com.citrus.pixel.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.cytrus.pixelarr.R

object ButtonStyles {
    val customFontFamily = FontFamily(Font(R.font.pixellari))
    val defaultTextColor = Color.Black
    val defaultTextSize = 16.sp
    val defaultBorderWidth = 1.dp
    val defaultCornerRadius = 4.dp
}

@Composable
fun PixelButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textColor: Color = ButtonStyles.defaultTextColor,
    textSize: TextUnit = ButtonStyles.defaultTextSize,
    containerColor: Color = colorResource(id = R.color.button),
    shape: Shape = RoundedCornerShape(ButtonStyles.defaultCornerRadius),
    content: @Composable RowScope.() -> Unit = {
        Text(
            text = text,
            fontFamily = ButtonStyles.customFontFamily,
            color = textColor,
            fontSize = textSize
        )
    }
) {
    OutlinedButton(
        onClick = onClick,
        border = BorderStroke(ButtonStyles.defaultBorderWidth, Color.Black),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = containerColor,
            contentColor = textColor
        ),
        shape = shape,
        modifier = modifier
    ) {
        content()
    }
}

@Composable
fun SliderNavigationButton(
    text: String,
    onClick: () -> Unit,
    textColor: Color = ButtonStyles.defaultTextColor,
    textSize: TextUnit = ButtonStyles.defaultTextSize,
    buttonWidth: Dp = 100.dp,
    buttonHeight: Dp = 40.dp
) {
    PixelButton(
        text = text,
        onClick = onClick,
        textColor = textColor,
        textSize = textSize,
        modifier = Modifier.size(width = buttonWidth, height = buttonHeight)
    )
}

@Composable
fun CustomButton(text: String, onClick: () -> Unit) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val buttonWidth = screenWidth * 0.8f
    val buttonHeight = screenHeight * 0.12f
    val shadowOffset = 5.dp
    val containerColor = colorResource(id = R.color.button)
    val buttonShadow = colorResource(id = R.color.buttonShadow)

    Box(modifier = Modifier.size(buttonWidth + shadowOffset, buttonHeight + shadowOffset)) {
        // Shadow box
        Box(
            modifier = Modifier
                .size(buttonWidth, buttonHeight)
                .offset(x = shadowOffset, y = shadowOffset)
                .background(buttonShadow)
                .zIndex(0f)
        )

        PixelButton(
            text = text,
            onClick = onClick,
            containerColor = containerColor,
            shape = RectangleShape,
            textSize = 26.sp,
            modifier = Modifier
                .size(buttonWidth, buttonHeight)
                .border(width = 2.dp, color = Color.Black)
                .zIndex(1f)
        ) {
            Text(
                text = text,
                fontFamily = ButtonStyles.customFontFamily,
                fontSize = 26.sp,
                color = Color.Black,
                lineHeight = 26.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}