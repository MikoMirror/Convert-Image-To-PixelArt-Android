package com.citrus.pixel.utils
import android.content.Context
import android.provider.Settings
fun areAnimationsEnabled(context: Context): Boolean {
    val durationScale = Settings.Global.getFloat(
        context.contentResolver,
        Settings.Global.ANIMATOR_DURATION_SCALE,
        1f
    )
    return durationScale > 0
}