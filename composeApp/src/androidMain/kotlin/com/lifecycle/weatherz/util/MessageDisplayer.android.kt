package com.lifecycle.weatherz.util

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun AndroidShowMessage(message: String) {
    val context = LocalContext.current
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

actual fun showMessage(message: String) {
    println("ShowMessage: $message")
}