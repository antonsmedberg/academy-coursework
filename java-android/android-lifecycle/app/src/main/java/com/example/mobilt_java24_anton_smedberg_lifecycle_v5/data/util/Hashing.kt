package com.example.mobilt_java24_anton_smedberg_lifecycle_v5.data.util

import java.security.MessageDigest
import java.util.Base64
import kotlin.random.Random

object Hashing {
    fun randomSalt(): String {
        val bytes = Random.Default.nextBytes(16)
        return Base64.getEncoder().encodeToString(bytes)
    }

    fun sha256(bytes: ByteArray): ByteArray {
        return MessageDigest.getInstance("SHA-256").digest(bytes)
    }

    fun hashPassword(plain: String, salt: String): String {
        // Viktigt: här ska *variabeln* plain användas, inte en sträng "plain"
        val input = "$plain:$salt".toByteArray(Charsets.UTF_8)
        val digest = sha256(input)
        return digest.joinToString("") { "%02x".format(it) } // hex
    }
}