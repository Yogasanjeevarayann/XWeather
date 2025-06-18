package com.lifecycle.weatherz.network


import com.lifecycle.weatherz.domain.WeatherEntityAPI
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

class WeatherApi(private val client: HttpClient) {
    suspend fun getWeather(lat: Double, lon: Double): WeatherEntityAPI {
        val response: HttpResponse = client.get("https://api.open-meteo.com/v1/forecast") {
            parameter("latitude", lat)
            parameter("longitude", lon)
            parameter("current_weather", true)

            // Print the final URL just before the request is sent
            url { println("WeatherApi → URL: ${buildString()}") }
        }

        return response.body()
    }
}
