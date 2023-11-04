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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
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
        modifier = Modifier.fillMaxSize().background(Color.White),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier.padding(bottom = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ClippedCornerButton(text = "Convert Image to PixelArt") {
                val pickIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
                    type = "image/*"
                }
                imagePicker.launch(pickIntent)
            }
            ClippedCornerButton(text = "New Empty Project") {}
            ClippedCornerButton(text = "Load Project") {}
        }
    }
}

//-------------------------------------------------------------------------------------------------

@Composable
fun ClippedCornerButton(text: String, onClick: () -> Unit) {
    val customFontFamily = FontFamily(Font(R.font.pixellari))
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    val buttonWidth = screenWidth * 0.9f
    val buttonHeight = screenHeight * 0.1f
    val fontSizeValue = screenHeight.value * 0.030f

    Button(
        onClick = onClick,
        modifier = Modifier.size(buttonWidth, buttonHeight),
        shape = ClippedCornerShape(clipDp = 20),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F94C0))
    ) {
        Text(text, fontFamily = customFontFamily, fontSize = fontSizeValue.sp)
    }
}