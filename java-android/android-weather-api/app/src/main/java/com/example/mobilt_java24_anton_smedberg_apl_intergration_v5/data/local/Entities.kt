package com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "cities",
    indices = [Index(value = ["name", "latitude", "longitude"], unique = true)]
)
data class CityEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val country: String?,
    val admin1: String?,
    val latitude: Double,
    val longitude: Double
)

@Entity(tableName = "weather_cache")
data class WeatherCacheEntity(
    @PrimaryKey val cityId: Long,
    val timestamp: Long,
    val currentTemp: Double,
    val rainProbNextHour: Int
)

@Entity(
    tableName = "recent_cities",
    primaryKeys = ["name", "latitude", "longitude"]
)
data class RecentCityEntity(
    val name: String,
    val country: String?,
    val admin1: String?,
    val latitude: Double,
    val longitude: Double,
    val lastVisited: Long
)