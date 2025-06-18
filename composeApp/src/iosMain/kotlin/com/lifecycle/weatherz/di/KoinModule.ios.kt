package com.lifecycle.weatherz.di

import com.lifecycle.weatherz.database.getDatabaseBuilder
import org.koin.dsl.module

actual val targetModule = module {
    single { getDatabaseBuilder() }
}