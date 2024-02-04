package com.citrus.pixel.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cytrus.pixelarr.R

@Composable
fun SliderNavigationButton(
    text: String,
    onClick: () -> Unit,
    textColor: Color = Color.Black,
    textSize: TextUnit = 16.sp,
    buttonWidth: Dp = 100.dp,
    buttonHeight: Dp = 8.dp
) {
    val customFontFamily = FontFamily(Font(R.font.pixellari))
    OutlinedButton(
        onClick = onClick,
        border = BorderStroke(1.dp, Color.Black),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color(0xFFFCFFED),
            contentColor = textColor
        ),
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .then(
                if (buttonWidth != 100.dp && buttonHeight != 8.dp) {
                    Modifier.size(width = buttonWidth, height = buttonHeight)
                } else Modifier
            )
    ) {
        Text(
            text = text,
            fontFamily = customFontFamily,
            color = textColor,
            fontSize = textSize
        )
    }
}

@Composable
fun CustomButton(text: String, onClick: () -> Unit) {
    val customFontFamily = FontFamily(Font(R.font.pixellari))
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val buttonWidth = screenWidth * 0.8f
    val buttonHeight = screenHeight * 0.12f
    val cornerRadius = 12.dp

    Button(
        onClick = onClick,
        modifier = Modifier
            .size(buttonWidth, buttonHeight)
            .border(width = 2.dp, color = Color.Black, shape = RoundedCornerShape(cornerRadius)),
        shape = RoundedCornerShape(cornerRadius),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFFCFFED)
        )
    ) {
        Text(
            text = text,
            fontFamily = customFontFamily,
            fontSize = 26.sp,
            color = Color.Black,
            lineHeight = 26.sp,
            textAlign = TextAlign.Center
        )
    }
}