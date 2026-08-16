package com.example.cache

// Funktion för att justera cachestorlek till närmaste potentiella 2
fun adjustCacheSize(size: Int): Int {
    var adjustedSize = size - 1
    adjustedSize = adjustedSize or (adjustedSize shr 1)
    adjustedSize = adjustedSize or (adjustedSize shr 2)
    adjustedSize = adjustedSize or (adjustedSize shr 4)
    adjustedSize = adjustedSize or (adjustedSize shr 8)
    adjustedSize = adjustedSize or (adjustedSize shr 16)
    return adjustedSize + 1
}

// Enkel cache-implementation
val cache = mutableMapOf<String, String>()

// Funktion för att lägga till data i cachen
fun addToCache(key: String, value: String) {
    cache[key] = value
}

// Funktion för att hämta data från cachen
fun getFromCache(key: String): String? {
    return cache[key]
}
