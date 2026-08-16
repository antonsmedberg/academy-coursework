package com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.domain.model

data class City(
    val id: Long = 0L,
    val name: String,
    val country: String?,
    val admin1: String?,
    val latitude: Double,
    val longitude: Double
)