package com.lifecycle.weatherz.presentation.screen.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifecycle.weatherz.data.WeatherDatabase
import com.lifecycle.weatherz.domain.City
import com.lifecycle.weatherz.domain.CurrentWeather
import com.lifecycle.weatherz.domain.CurrentWeatherUnits
import com.lifecycle.weatherz.domain.WeatherEntity
import com.lifecycle.weatherz.domain.WeatherEntityAPI
import com.lifecycle.weatherz.network.WeatherApi
import com.lifecycle.weatherz.util.RequestState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val database: WeatherDatabase,
    private val api: WeatherApi
) : ViewModel() {

    private var _requestState = MutableStateFlow<RequestState<WeatherEntity>>(RequestState.Idle)
    val requestState: StateFlow<RequestState<WeatherEntity>> = _requestState

    fun fetchDummyWeather() {
        println("ViewModel → fetchDummyWeather() called")
        val cities = listOf(
            City("New York", 40.7128, -74.0060),
            City("London", 51.5074, -0.1278),
            City("Tokyo", 35.6762, 139.6503),
            City("Sydney", -33.8688, 151.2093),
            City("Paris", 48.8566, 2.3522),
            City("Delhi", 28.6139, 77.2090),
            City("Moscow", 55.7558, 37.6173),
            City("Rio de Janeiro", -22.9068, -43.1729),
            City("Cape Town", -33.9249, 18.4241)
        )
        viewModelScope.launch {
            try {
                _requestState.value = RequestState.Loading
                val city = cities.random()
                println("ViewModel → Fetching weather for ${city.name}")
                val response = api.getWeather(city.lat, city.lon)
                val weather = response.toEntity()
                _requestState.value = RequestState.Success(weather)
                println("ViewModel → Success: $weather")
            } catch (e: Exception) {
                val error = e.message ?: "Unknown error"
                println("ViewModel → Error: $error")
                _requestState.value = RequestState.Error(error)
            }
        }
    }

    fun saveDummyWeather(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        println("ViewModel → saveDummyWeather() called")

        viewModelScope.launch {
            try {
                val state = _requestState.value
                if (state is RequestState.Success) {
                    println("ViewModel → Saving weather: ${state.data}")
                    database.weatherDao().insertWeather(state.data)
                    println("ViewModel → Weather saved successfully")
                    onSuccess()
                } else {
                    val message = "No weather data to save."
                    println("ViewModel → Error: $message")
                    onError(message)
                }
            } catch (e: Exception) {
                val errorMsg = e.message ?: "Unknown error"
                println("ViewModel → Exception: $errorMsg")
                onError(errorMsg)
            }
        }
    }
    fun WeatherEntityAPI.toEntity(): WeatherEntity {
        return WeatherEntity(
            latitude = latitude,
            longitude = longitude,
            generationtime_ms = generationtimeMs,
            utc_offset_seconds = utcOffsetSeconds,
            timezone = timezone,
            timezone_abbreviation = timezoneAbbreviation,
            elevation = elevation.toInt(),
            currentWeatherUnits = CurrentWeatherUnits(
                time = currentWeatherUnits.time,
                interval = currentWeatherUnits.interval,
                temperature = currentWeatherUnits.temperature,
                windspeed = currentWeatherUnits.windspeed,
                winddirection = currentWeatherUnits.winddirection,
                is_day = currentWeatherUnits.isDay,
                weathercode = currentWeatherUnits.weathercode
            ),
            currentWeather = CurrentWeather(
                time = currentWeather.time,
                interval = currentWeather.interval,
                temperature = currentWeather.temperature,
                windspeed = currentWeather.windspeed,
                winddirection = currentWeather.winddirection,
                is_day = currentWeather.isDay,
                weathercode = currentWeather.weathercode
            )
        )
    }

}
