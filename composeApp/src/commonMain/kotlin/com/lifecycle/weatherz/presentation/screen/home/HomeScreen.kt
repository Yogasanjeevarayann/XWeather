package com.lifecycle.weatherz.presentation.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lifecycle.weatherz.util.DisplayResult
import com.lifecycle.weatherz.util.RequestState
import com.lifecycle.weatherz.util.SmootherShape
import com.lifecycle.weatherz.util.TailwindCSSColor
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import weatherz.composeapp.generated.resources.Res
import weatherz.composeapp.generated.resources.black_cloud_snow_rain
import weatherz.composeapp.generated.resources.black_cloud_white_cloud_rain
import weatherz.composeapp.generated.resources.compass_symbol
import weatherz.composeapp.generated.resources.history
import weatherz.composeapp.generated.resources.rainbow_white_cloud_rain
import weatherz.composeapp.generated.resources.thunder_moon_white_cloud
import weatherz.composeapp.generated.resources.timer_icon
import weatherz.composeapp.generated.resources.white_and_blue_cloud
import weatherz.composeapp.generated.resources.white_cloud_black_cloud_snow
import weatherz.composeapp.generated.resources.wind_direction
import weatherz.composeapp.generated.resources.wind_icon
import weatherz.composeapp.generated.resources.wind_snow_white_cloud_sun
import weatherz.composeapp.generated.resources.wind_sun_white_cloud
import weatherz.composeapp.generated.resources.wind_white_cloud_thunder_rain

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onHistoryClick: () -> Unit,
    showMessage: (String) -> Unit
) {
    val viewModel = koinInject<WeatherViewModel>()
    val state = viewModel.requestState.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.fetchDummyWeather()
        viewModel.requestState.collectLatest { state ->
            if (state is RequestState.Success) {
                viewModel.saveDummyWeather(
                    onSuccess = { showMessage("Weather auto-saved!") },
                    onError = { showMessage(it) }
                )
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("WeatherX", fontWeight = FontWeight.Black, modifier = Modifier.padding(start = 5.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = TailwindCSSColor.Gray200),
                actions = {
                    IconButton(onClick = onHistoryClick, modifier = Modifier.padding(end = 15.dp),) {
                        Surface(
                            shape = CircleShape,
                            color = Color.White
                        ) {
                            Icon(
                                painter = painterResource(resource = Res.drawable.history),
                                contentDescription = "History",
                                tint = Color.Black,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(TailwindCSSColor.Gray200)
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            state.value.DisplayResult(
                onIdle = {
                    Text("Loading weather...")
                },
                onLoading = {
                    CircularProgressIndicator(color = Color.Black)
                },
                onError = {
                    Text("Error: $it", color = MaterialTheme.colorScheme.error)
                },
                onSuccess = { weather ->
                    val weatherImage = getWeatherImage(weather.currentWeather.weathercode)
                    val temperature = weather.currentWeather.temperature
                    val description = getWeatherDescription(weather.currentWeather.weathercode)
                    val wind = weather.currentWeather.windspeed
                    val direction = weather.currentWeather.winddirection
                    val interval = weather.currentWeather.interval

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Image with double-tap gesture to fetch again
                        Box(
                            modifier = Modifier
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onDoubleTap = {
                                            viewModel.fetchDummyWeather()
                                            viewModel.saveDummyWeather(
                                                onSuccess = { showMessage("Updated!") },
                                                onError = { showMessage(it) }
                                            )
                                        }
                                    )
                                }
                        ) {
                            Image(
                                painter = weatherImage,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(200.dp)
                                    .padding(8.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        // Temperature
                        Text(
                            text = "$temperature°C",
                            fontSize = 80.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Black
                        )

                        // Weather Description
                        Text(
                            text = description,
                            fontSize = 18.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                        // Info Row
                        Text(
                            text = "Double-tap the weather image to refresh",
                            fontSize = 12.sp,
                            color = TailwindCSSColor.Gray400,
                            modifier = Modifier.padding(top = 4.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            WeatherStatCard(
                                iconRes = Res.drawable.wind_icon,
                                value = "$wind km/h",
                                label = "Wind"
                            )
                            WeatherStatCard(
                                iconRes = Res.drawable.wind_direction,
                                value = "$direction°",
                                label = "Direction"
                            )
                            WeatherStatCard(
                                iconRes = Res.drawable.timer_icon,
                                value = "${interval}s",
                                label = "Interval"
                            )
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun WeatherStatCard(
    iconRes: DrawableResource,
    value: String,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(100.dp)
            .height(100.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFe0f7fa), // Light cyan
                        Color(0xFFb2ebf2)  // Slightly deeper cyan
                    )
                ),
                shape = SmootherShape(
                    radius = 23.dp,
                    smoothness = 1f
                )
            )
            .padding(12.dp)
    ) {
        Icon(
            painter = painterResource(resource = iconRes),
            contentDescription = label,
            tint = Color.Black,
            modifier = Modifier.size(28.dp)
        )
        Text(value, fontWeight = FontWeight.Bold)
        Text(label, style = MaterialTheme.typography.bodySmall,color=Color.Gray)
    }
}

@Composable
fun getWeatherImage(code: Int): Painter {
    val imageName = when {
        code == 0 -> "wind_sun_white_cloud"
        code in 1..3 -> "white_and_blue_cloud"
        code in 45..48 -> "compass_symbol"
        code in 51..55 || code in 61..65 -> "black_cloud_white_cloud_rain"
        code in 56..57 || code in 66..67 -> "black_cloud_snow_rain"
        code in 71..75 -> "white_cloud_black_cloud_snow"
        code == 77 || code == 85 || code == 86 -> "wind_snow_white_cloud_sun"
        code in 80..82 -> "rainbow_white_cloud_rain"
        code == 95 -> "wind_white_cloud_thunder_rain"
        code == 96 || code == 99 -> "thunder_moon_white_cloud"
        else -> "white_and_blue_cloud"
    }

    val resourceId = when (imageName) {
        "wind_sun_white_cloud" -> Res.drawable.wind_sun_white_cloud
        "white_and_blue_cloud" -> Res.drawable.white_and_blue_cloud
        "compass_symbol" -> Res.drawable.compass_symbol
        "black_cloud_white_cloud_rain" -> Res.drawable.black_cloud_white_cloud_rain
        "black_cloud_snow_rain" -> Res.drawable.black_cloud_snow_rain
        "white_cloud_black_cloud_snow" -> Res.drawable.white_cloud_black_cloud_snow
        "wind_snow_white_cloud_sun" -> Res.drawable.wind_snow_white_cloud_sun
        "rainbow_white_cloud_rain" -> Res.drawable.rainbow_white_cloud_rain
        "wind_white_cloud_thunder_rain" -> Res.drawable.wind_white_cloud_thunder_rain
        "thunder_moon_white_cloud" -> Res.drawable.thunder_moon_white_cloud
        else -> Res.drawable.white_and_blue_cloud
    }

    return painterResource(resource = resourceId)
}


fun getWeatherDescription(code: Int): String {
    return when {
        code == 0 -> "Clear"
        code in 1..3 -> "Cloudy"
        code in 45..48 -> "Fog"
        code in 51..55 || code in 61..65 -> "Rain"
        code in 56..57 || code in 66..67 -> "Freezing Rain"
        code in 71..75 -> "Snow"
        code == 77 -> "Snow Grains"
        code in 80..82 -> "Showers"
        code == 85 || code == 86 -> "Snow Showers"
        code == 95 -> "Thunder"
        code == 96 || code == 99 -> "Hailstorm"
        else -> "Unknown"
    }
}

