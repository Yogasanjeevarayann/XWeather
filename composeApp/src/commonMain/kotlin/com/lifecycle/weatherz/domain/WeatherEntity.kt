package com.lifecycle.weatherz.domain

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class WeatherEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // use auto-generated ID instead of latitude

    val latitude: Double,
    val longitude: Double,
    val generationtime_ms: Double,
    val utc_offset_seconds: Int,
    val timezone: String,
    val timezone_abbreviation: String,
    val elevation: Int,

    @Embedded(prefix = "units_")
    val currentWeatherUnits: CurrentWeatherUnits,

    @Embedded(prefix = "weather_")
    val currentWeather: CurrentWeather
)

data class CurrentWeatherUnits(
    val time: String,
    val interval: String,
    val temperature: String,
    val windspeed: String,
    val winddirection: String,
    val is_day: String,
    val weathercode: String
)

data class CurrentWeather(
    val time: String,
    val interval: Int,
    val temperature: Double,
    val windspeed: Double,
    val winddirection: Int,
    val is_day: Int,
    val weathercode: Int
)