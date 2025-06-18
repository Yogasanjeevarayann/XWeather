package com.lifecycle.weatherz.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lifecycle.weatherz.data.WeatherDatabase

fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<WeatherDatabase> {
    val dbFile = context.getDatabasePath("weather.db")
    return Room.databaseBuilder(
        context = context,
        name = dbFile.absolutePath
    )
}