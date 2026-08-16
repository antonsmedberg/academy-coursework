// data/remote/OpenMeteoApi.kt
package com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenMeteoApi {
    @GET("v1/forecast")
    suspend fun forecast(
        @Query("latitude") lat: Double,
        @Query("longitude") lon: Double,
        // nuvarande temp
        @Query("current") current: String = "temperature_2m",
        // kan behållas för “just nu”/nästa timme (om du vill)
        @Query("hourly") hourly: String = "temperature_2m,precipitation_probability",
        // NYTT: daglig sammanfattning för bättre UX
        @Query("daily")
        daily: String = "temperature_2m_max,temperature_2m_min,precipitation_probability_max,weathercode",
        @Query("timezone") timezone: String = "auto"
    ): ForecastDto
}

@Serializable
data class ForecastDto(
    val current: Current? = null,
    val hourly: Hourly? = null,
    val daily: Daily? = null
) {
    @Serializable
    data class Current(
        @SerialName("temperature_2m") val temperature2m: Double = 0.0
    )

    @Serializable
    data class Hourly(
        val time: List<String> = emptyList(),
        @SerialName("temperature_2m") val temperature2m: List<Double> = emptyList(),
        @SerialName("precipitation_probability") val precipitationProbability: List<Int> = emptyList()
    )

    @Serializable
    data class Daily(
        val time: List<String> = emptyList(), // ISO "yyyy-MM-dd"
        @SerialName("temperature_2m_max") val tMax: List<Double> = emptyList(),
        @SerialName("temperature_2m_min") val tMin: List<Double> = emptyList(),
        @SerialName("precipitation_probability_max") val rainProbMax: List<Int> = emptyList(),
        val weathercode: List<Int> = emptyList()
    )
}