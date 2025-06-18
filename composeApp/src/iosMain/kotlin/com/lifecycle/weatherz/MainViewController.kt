package com.lifecycle.weatherz

import androidx.compose.ui.window.ComposeUIViewController
import com.lifecycle.weatherz.di.initializeKoin

fun MainViewController() = ComposeUIViewController(
    configure = { initializeKoin() }
) { App() }