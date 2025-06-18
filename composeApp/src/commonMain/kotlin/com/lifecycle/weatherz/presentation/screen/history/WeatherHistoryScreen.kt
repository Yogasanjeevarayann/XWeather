package com.lifecycle.weatherz.presentation.screen.history

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lifecycle.weatherz.domain.WeatherEntity
import com.lifecycle.weatherz.presentation.components.LoadingView
import org.koin.compose.koinInject
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.lifecycle.weatherz.presentation.screen.home.getWeatherDescription
import com.lifecycle.weatherz.presentation.screen.home.getWeatherImage
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.painterResource
import weatherz.composeapp.generated.resources.Res
import weatherz.composeapp.generated.resources.back
import weatherz.composeapp.generated.resources.trash

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherHistoryScreen(
    onBackClick: () -> Unit
) {
    val viewModel = koinInject<WeatherHistoryViewModel>()
    val history = viewModel.weatherList
    val selectedItems = viewModel.selectedItems
    val isLoading = viewModel.isLoading

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = if(selectedItems.isEmpty()) "Weather History" else "${selectedItems.size} selected", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(resource = Res.drawable.back),
                            contentDescription = "Back",
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                },
                actions = {
                    if (selectedItems.isNotEmpty()) {
                        IconButton(onClick = { viewModel.deleteSelected() }) {
                            Icon(
                                painter = painterResource(resource = Res.drawable.trash),
                                contentDescription = "Delete Selected",
                                modifier = Modifier.padding(8.dp)

                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when {
                isLoading -> LoadingView()
                history.isEmpty() -> Text("No history found.", modifier = Modifier.align(Alignment.Center))
                else -> LazyColumn(modifier = Modifier.padding(16.dp)) {
                    items(history) { weather ->
                        WeatherHistoryItem(
                            weather = weather,
                            isSelected = selectedItems.contains(weather.id.toLong()),
                            onLongPress = { viewModel.toggleSelection(weather.id.toLong()) }
                        )
                        Divider()
                    }
                }
            }
        }
    }
}

@Composable
fun WeatherHistoryItem(
    weather: WeatherEntity,
    isSelected: Boolean,
    onLongPress: () -> Unit
) {
    val weatherCode = weather.currentWeather.weathercode
    val image = getWeatherImage(weatherCode)
    val description = getWeatherDescription(weatherCode)
    val time = parseTime(weather.currentWeather.time)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (isSelected) Color.LightGray else Color.Transparent)
            .padding(8.dp)
            .combinedClickable(
                onClick = {},
                onLongClick = onLongPress
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Day + Time
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(60.dp)) {
            Text(time.day, fontWeight = FontWeight.Bold)
            Text(time.hour, style = MaterialTheme.typography.bodySmall)
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Image + Description
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
            Image(
                painter = image,
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )
            Text(description, fontSize = 14.sp, color = Color.Gray)
        }

        // Temperature
        Text("${weather.currentWeather.temperature}°C", fontWeight = FontWeight.Bold)
    }
}

data class ParsedTime(val day: String, val hour: String)


fun parseTime(isoTime: String): ParsedTime {
    return try {
        val fixedIsoTime = "$isoTime:00Z" // Append seconds + Z (UTC)
        val instant = Instant.parse(fixedIsoTime)
        val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

        val day = dateTime.dayOfWeek.name.take(3)
        val hour = formatHour(dateTime.time)
        ParsedTime(day, hour)
    } catch (e: Exception) {
        println("parseTime error: ${e.message}")
        ParsedTime("N/A", "N/A")
    }
}

// Helper to convert LocalTime to "hh AM/PM"
fun formatHour(time: LocalTime): String {
    val hour = time.hour
    val amPm = if (hour < 12) "AM" else "PM"
    val hour12 = if (hour % 12 == 0) 12 else hour % 12
    return "$hour12 $amPm"
}