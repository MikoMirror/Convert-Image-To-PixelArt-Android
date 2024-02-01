package com.citrus.pixel.ui

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.citrus.pixel.viewmodels.ConvertActivity
import com.cytrus.pixelarr.R


@Composable
fun StartScreenUI() {
    val context = LocalContext.current
    val imagePicker = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
        val imageUri = result.data?.data?.toString()
        if (result.resultCode == Activity.RESULT_OK && imageUri != null) {
            val intent = Intent(context, ConvertActivity::class.java).apply {
                putExtra("imageUri", imageUri)
            }
            context.startActivity(intent)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF000000)),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.background2),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CustomButton(text = "Convert Image to PixelArt") {
                val pickIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
                    type = "image/*"
                }
                imagePicker.launch(pickIntent)
            }
        }
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