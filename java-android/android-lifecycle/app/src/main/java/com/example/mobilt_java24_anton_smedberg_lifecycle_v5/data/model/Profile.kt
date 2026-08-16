package com.example.mobilt_java24_anton_smedberg_lifecycle_v5.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profiles")
data class Profile(
    @PrimaryKey val userId: Long,
    val age: Int,
    val hasDriverLicense: Boolean,
    val gender: String,     // "kvinna" | "man" | "annat"
    val phone: String,
    val email: String
)