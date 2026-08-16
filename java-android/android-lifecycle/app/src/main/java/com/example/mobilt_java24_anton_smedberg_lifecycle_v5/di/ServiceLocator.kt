package com.example.mobilt_java24_anton_smedberg_lifecycle_v5.di

import android.app.Application
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.example.mobilt_java24_anton_smedberg_lifecycle_v5.data.db.AppDatabase
import com.example.mobilt_java24_anton_smedberg_lifecycle_v5.data.prefs.SessionPrefs
import com.example.mobilt_java24_anton_smedberg_lifecycle_v5.data.repo.AuthRepository
import com.example.mobilt_java24_anton_smedberg_lifecycle_v5.data.repo.ProfileRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

object ServiceLocator {
    private val appScope = CoroutineScope(SupervisorJob())

    private var initialized = false
    private lateinit var database: AppDatabase
    private lateinit var _sessionPrefs: SessionPrefs
    private lateinit var _authRepo: AuthRepository
    private lateinit var _profileRepo: ProfileRepository

    fun init(app: Application) {
        if (initialized) return  // jag gör init idempotent
        val appCtx = app.applicationContext

        val ds = PreferenceDataStoreFactory.create(
            scope = appScope,
            produceFile = { appCtx.preferencesDataStoreFile("session") }
        )
        _sessionPrefs = SessionPrefs(ds)

        database = Room.databaseBuilder(appCtx, AppDatabase::class.java, "lifecycle_v5.db")
            .fallbackToDestructiveMigration(false)
            .build()

        _authRepo = AuthRepository(database.userDao())
        _profileRepo = ProfileRepository(database.profileDao())

        initialized = true
    }

    // Publika beroenden (enkel och tydlig yta)
    val sessionPrefs: SessionPrefs get() = _sessionPrefs
    val authRepo: AuthRepository get() = _authRepo
    val profileRepo: ProfileRepository get() = _profileRepo
}