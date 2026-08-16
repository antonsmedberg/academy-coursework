package com.example.mobilt_java24_anton_smedberg_lifecycle_v5.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mobilt_java24_anton_smedberg_lifecycle_v5.data.model.Profile

@Dao
interface ProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(profile: Profile)

    @Update
    suspend fun update(profile: Profile)

    @Query("SELECT * FROM profiles WHERE userId = :userId")
    suspend fun get(userId: Long): Profile?
}