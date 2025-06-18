package com.lifecycle.weatherz.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.lifecycle.weatherz.domain.WeatherEntity

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: WeatherEntity)

    @Query("SELECT * FROM WeatherEntity ORDER BY id DESC")
    suspend fun getAllWeather(): List<WeatherEntity>

    @Update
    suspend fun updateWeather(weather: WeatherEntity)

    @Delete
    suspend fun deleteWeather(weather: WeatherEntity)
}

