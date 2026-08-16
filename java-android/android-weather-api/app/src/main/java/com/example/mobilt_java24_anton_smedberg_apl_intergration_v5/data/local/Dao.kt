package com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {
    @Query("SELECT * FROM cities ORDER BY name")
    suspend fun list(): List<CityEntity>

    // Lyssna på databasen för att observera favoriter reaktivt
    @Query("SELECT * FROM cities ORDER BY name")
    fun observeList(): Flow<List<CityEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIgnore(c: CityEntity): Long

    @Query("SELECT id FROM cities WHERE name = :name AND latitude = :lat AND longitude = :lon LIMIT 1")
    suspend fun getIdByKey(name: String, lat: Double, lon: Double): Long?

    @Delete
    suspend fun delete(c: CityEntity)

    @Query("DELETE FROM cities WHERE name = :name AND latitude = :lat AND longitude = :lon")
    suspend fun deleteByKey(name: String, lat: Double, lon: Double): Int
}

@Dao
interface RecentCityDao {
    @Query("SELECT * FROM recent_cities ORDER BY lastVisited DESC LIMIT :limit")
    suspend fun listRecent(limit: Int): List<RecentCityEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: RecentCityEntity)
}

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weather_cache WHERE cityId = :cityId LIMIT 1")
    suspend fun get(cityId: Long): WeatherCacheEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(c: WeatherCacheEntity)
}