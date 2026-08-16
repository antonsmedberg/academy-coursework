package com.example.mobilt_java24_anton_smedberg_lifecycle_v5.data.repo

import com.example.mobilt_java24_anton_smedberg_lifecycle_v5.data.db.ProfileDao
import com.example.mobilt_java24_anton_smedberg_lifecycle_v5.data.model.Profile

class ProfileRepository(private val dao: ProfileDao) {
    suspend fun save(profile: Profile) { dao.upsert(profile) }
    suspend fun get(userId: Long): Profile? = dao.get(userId)
}