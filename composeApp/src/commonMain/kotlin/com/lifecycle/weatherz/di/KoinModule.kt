package com.lifecycle.weatherz.di

import com.lifecycle.weatherz.data.getRoomDatabase
import com.lifecycle.weatherz.network.WeatherApi
import com.lifecycle.weatherz.presentation.screen.history.WeatherHistoryViewModel
import com.lifecycle.weatherz.presentation.screen.home.WeatherViewModel
import com.lifestyle.weatherx.network.createHttpClient
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.core.module.dsl.*

expect val targetModule: Module

val sharedModule = module {
    single { createHttpClient() }
    single { WeatherApi(get()) }
    single { getRoomDatabase(get()) }

    viewModel { WeatherViewModel(get(),get()) }
    viewModel { WeatherHistoryViewModel(get()) }
}

fun initializeKoin(
    config: (KoinApplication.() -> Unit)? = null
) {
    startKoin {
        config?.invoke(this)
        modules(targetModule, sharedModule)
    }
}