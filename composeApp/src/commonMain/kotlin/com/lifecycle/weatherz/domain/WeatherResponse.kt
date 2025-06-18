package com.lifecycle.weatherz.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class WeatherEntityAPI(
    val latitude: Double,
    val longitude: Double,
    @SerialName("generationtime_ms") val generationtimeMs: Double,
    @SerialName("utc_offset_seconds") val utcOffsetSeconds: Int,
    val timezone: String,
    @SerialName("timezone_abbreviation") val timezoneAbbreviation: String,
    val elevation: Double,
    @SerialName("current_weather_units") val currentWeatherUnits: CurrentWeatherUnitsAPI,
    @SerialName("current_weather") val currentWeather: CurrentWeatherAPI
)

@Serializable
data class CurrentWeatherUnitsAPI(
    val time: String,
    val interval: String,
    val temperature: String,
    val windspeed: String,
    val winddirection: String,
    @SerialName("is_day") val isDay: String,
    val weathercode: String
)

@Serializable
data class CurrentWeatherAPI(
    val time: String,
    val interval: Int,
    val temperature: Double,
    val windspeed: Double,
    val winddirection: Int,
    @SerialName("is_day") val isDay: Int,
    val weathercode: Int
)