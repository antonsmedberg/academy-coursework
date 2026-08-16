package com.example.api

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import kotlinx.coroutines.*

// Bitmaskor för statuskoder
const val STATUS_SUCCESS = 0x1
const val STATUS_CLIENT_ERROR = 0x2
const val STATUS_SERVER_ERROR = 0x4

// Funktion för att kategorisera statuskoder
fun categorizeStatusCode(status: Int): Int {
    return when {
        status in 200..299 -> STATUS_SUCCESS
        status in 400..499 -> STATUS_CLIENT_ERROR
        status in 500..599 -> STATUS_SERVER_ERROR
        else -> 0
    }
}

// Coroutine-baserad funktion för att hämta data från API
suspend fun fetchApiData(url: String): String {
    val client = HttpClient(CIO)
    return try {
        val response: HttpResponse = client.get(url)
        val status = categorizeStatusCode(response.status.value)
        when {
            (status and STATUS_SUCCESS) != 0 -> "Success: ${response.readText()}"
            (status and STATUS_CLIENT_ERROR) != 0 -> "Client error: ${response.status}"
            (status and STATUS_SERVER_ERROR) != 0 -> "Server error: ${response.status}"
            else -> "Unknown error: ${response.status}"
        }
    } catch (e: Exception) {
        "Network error: ${e.message}"
    } finally {
        client.close()
    }
}
