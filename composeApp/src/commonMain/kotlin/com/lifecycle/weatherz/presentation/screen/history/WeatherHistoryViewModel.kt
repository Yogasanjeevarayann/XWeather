package com.lifecycle.weatherz.presentation.screen.history

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifecycle.weatherz.data.WeatherDatabase
import com.lifecycle.weatherz.domain.WeatherEntity
import kotlinx.coroutines.launch

class WeatherHistoryViewModel(
    private val database: WeatherDatabase
) : ViewModel() {

    var weatherList by mutableStateOf<List<WeatherEntity>>(emptyList())
        private set

    var isLoading by mutableStateOf(true)
        private set

    var selectedItems by mutableStateOf<Set<Long>>(emptySet())
        private set

    init {
        fetchWeatherHistory()
    }

    private fun fetchWeatherHistory() {
        viewModelScope.launch {
            isLoading = true
            try {
                weatherList = database.weatherDao().getAllWeather()
            } catch (_: Exception) {
            } finally {
                isLoading = false
            }
        }
    }

    fun toggleSelection(id: Long) {
        selectedItems = if (selectedItems.contains(id)) {
            selectedItems - id
        } else {
            selectedItems + id
        }
    }

    fun deleteSelected() {
        viewModelScope.launch {
            val toDelete = weatherList.filter { it.id.toLong() in selectedItems }.toList()
            toDelete.forEach { weather ->
                database.weatherDao().deleteWeather(weather)
            }
            selectedItems = emptySet()
            fetchWeatherHistory()
        }
    }

}
