package com.lifecycle.weatherz.data

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.lifecycle.weatherz.data.dao.WeatherDao
import com.lifecycle.weatherz.domain.WeatherEntity
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json


@Database(
    entities = [WeatherEntity::class],
    version = 1,
    exportSchema = true
)
@ConstructedBy(WeatherDatabaseConstructor::class)
@TypeConverters(WeatherTypeConverters::class)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object WeatherDatabaseConstructor : RoomDatabaseConstructor<WeatherDatabase> {
    override fun initialize(): WeatherDatabase
}

class WeatherTypeConverters {
    @TypeConverter
    fun fromString(value: String): List<String> {
        return Json.decodeFromString(ListSerializer(String.serializer()), value)
    }

    @TypeConverter
    fun fromList(list: List<String>): String {
        return Json.encodeToString(ListSerializer(String.serializer()), list)
    }
}