package com.lifecycle.weatherz

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform