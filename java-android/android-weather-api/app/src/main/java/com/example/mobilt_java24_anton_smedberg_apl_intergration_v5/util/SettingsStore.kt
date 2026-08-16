// util/SettingsStore.kt
package com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.util

import android.app.Application
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val DATASTORE_NAME = "app_settings"

// DataStore måste leva på en Context – Application funkar fint.
private val Application.dataStore by preferencesDataStore(name = DATASTORE_NAME)

/**
 * Appens enkla inställningar (enhet + notiser).
 */
class SettingsStore(private val app: Application) {

    private object Keys {
        val USE_FAHRENHEIT = booleanPreferencesKey("use_fahrenheit")
        val ALERTS_ENABLED = booleanPreferencesKey("alerts_enabled")
    }

    // Behåll publika namn för kompatibilitet med övrig kod
    val useFahrenheitFlow: Flow<Boolean> =
        app.dataStore.data.map { it[Keys.USE_FAHRENHEIT] ?: false }

    val alertsEnabledFlow: Flow<Boolean> =
        app.dataStore.data.map { it[Keys.ALERTS_ENABLED] ?: false }

    suspend fun setUseFahrenheit(enabled: Boolean) {
        app.dataStore.edit { it[Keys.USE_FAHRENHEIT] = enabled }
    }

    suspend fun setAlertsEnabled(enabled: Boolean) {
        app.dataStore.edit { it[Keys.ALERTS_ENABLED] = enabled }
    }
}