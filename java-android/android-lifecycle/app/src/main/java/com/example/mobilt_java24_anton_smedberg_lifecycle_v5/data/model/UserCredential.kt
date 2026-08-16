package com.example.mobilt_java24_anton_smedberg_lifecycle_v5.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserCredential(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val username: String,
    val email: String,
    val personnummer: String,
    val salt: String,
    val passwordHash: String
)