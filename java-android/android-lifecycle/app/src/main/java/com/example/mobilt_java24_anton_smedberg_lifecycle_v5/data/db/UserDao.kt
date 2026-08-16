package com.example.mobilt_java24_anton_smedberg_lifecycle_v5.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mobilt_java24_anton_smedberg_lifecycle_v5.data.model.UserCredential

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(user: UserCredential): Long

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun findByUsername(username: String): UserCredential?

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getById(id: Long): UserCredential?

    @Query("SELECT COUNT(*) FROM users")
    suspend fun count(): Int
}