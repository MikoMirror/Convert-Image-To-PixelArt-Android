package com.citrus.pixel.ui

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.citrus.pixel.viewmodels.ConvertActivity
import com.cytrus.pixelarr.R
import kotlin.random.Random


@Composable
fun PixelBlocks(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxWidth().height(150.dp)) {
        val blockSize = 5.dp.toPx()
        val columns = (size.width / blockSize).toInt()
        val rows = (size.height / blockSize).toInt()

        for (col in 0 until columns) {
            for (row in 0 until rows) {
                val probability = 1 - (row.toFloat() / rows)
                if (Random.nextFloat() < probability) {
                    drawRect(
                        color = Color(0xFFE6A4B4),
                        topLeft = Offset(col * blockSize, row * blockSize),
                        size = Size(blockSize, blockSize)
                    )
                }
            }
        }
    }
}

@Composable
fun StartScreenUI() {
    val context = LocalContext.current
    val backgroundColor = colorResource(id = R.color.background)
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
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            PixelBlocks()

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

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

