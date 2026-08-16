package com.example.async

import kotlinx.coroutines.*

// Bitmaskor för att representera olika asynkrona operationer
const val FLAG_OPERATION_1 = 0x1
const val FLAG_OPERATION_2 = 0x2
const val FLAG_OPERATION_3 = 0x4

// Coroutine-baserad funktion för att utföra flera asynkrona operationer
suspend fun performAsyncOperations(): Int {
    var flags = 0
    coroutineScope {
        val job1 = async {
            delay(1000)
            flags = flags or FLAG_OPERATION_1
        }
        val job2 = async {
            delay(2000)
            flags = flags or FLAG_OPERATION_2
        }
        val job3 = async {
            delay(3000)
            flags = flags or FLAG_OPERATION_3
        }
        job1.await()
        job2.await()
        job3.await()
    }
    return flags
}

// Funktion för att kontrollera om alla operationer är slutförda
fun areAllOperationsComplete(flags: Int): Boolean {
    val allOperationsFlags = FLAG_OPERATION_1 or FLAG_OPERATION_2 or FLAG_OPERATION_3
    return (flags and allOperationsFlags) == allOperationsFlags
}
