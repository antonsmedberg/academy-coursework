package com.example.mobilt_java24_anton_smedberg_lifecycle_v5.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mobilt_java24_anton_smedberg_lifecycle_v5.data.model.Profile
import com.example.mobilt_java24_anton_smedberg_lifecycle_v5.data.model.UserCredential

@Database(entities = [UserCredential::class, Profile::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun profileDao(): ProfileDao
}