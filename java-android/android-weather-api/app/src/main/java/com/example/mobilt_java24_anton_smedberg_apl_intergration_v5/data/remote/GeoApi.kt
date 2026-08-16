// data/remote/GeoApi.kt
package com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Query

interface GeoApi {
    @GET("v1/search")
    suspend fun search(
        @Query("name") name: String,
        @Query("count") count: Int = 10,
        @Query("language") language: String, // <-- ingen default här
        @Query("format") format: String = "json"
    ): GeoResponse
}

@Serializable
data class GeoResponse(
    @SerialName("results") val results: List<GeoResult> = emptyList()
)

@Serializable
data class GeoResult(
    val name: String = "",
    val country: String? = null,
    val admin1: String? = null,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)