// domain/model/WeatherSnapshot.kt
package com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.domain.model

data class HourPoint(
    val timeIso: String,
    val temperature: Double,
    val precipitationProb: Int
)

data class DaySummary(
    val dateIso: String,      // "yyyy-MM-dd"
    val tMin: Double,
    val tMax: Double,
    val rainProbMax: Int,     // % (0..100)
    val weatherCode: Int      // Open-Meteo weathercode
)

data class WeatherSnapshot(
    val currentTemp: Double,
    val nextHourRainProb: Int,
    val hours: List<HourPoint>,     // kvar om du vill använda senare
    val days: List<DaySummary>      // NYTT: 5-dagars
)