package com.example.mobilt_java24_anton_smedberg_lifecycle_v5.data.prefs

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * Enkel sessions- och formulärutkast-hantering ovanpå DataStore.
 * Helt frikopplad från Context (Context hanteras vid skapande av DataStore i ServiceLocator).
 */
class SessionPrefs(
    private val dataStore: DataStore<Preferences>
) {
    private object Keys {
        val loggedInUserId = longPreferencesKey("loggedInUserId")

        // Draft-fält för formuläret
        val draftAge    = stringPreferencesKey("draftAge")
        val draftHasDL  = booleanPreferencesKey("draftHasDL")
        val draftGender = stringPreferencesKey("draftGender")
        val draftPhone  = stringPreferencesKey("draftPhone")
        val draftEmail  = stringPreferencesKey("draftEmail")
    }

    /** Flöde av inloggad användar-id (eller null). */
    val loggedInUserIdFlow: Flow<Long?> = dataStore.data.map { it[Keys.loggedInUserId] }

    /** Bekvämt bool-flöde för UI som vill toggla autologin-grejer. */
    val isLoggedInFlow: Flow<Boolean> = loggedInUserIdFlow.map { it != null }

    /** Sätt/ta bort inloggad användare (autologin). */
    suspend fun setLoggedInUserId(id: Long?) {
        dataStore.edit { prefs ->
            if (id == null) prefs.remove(Keys.loggedInUserId)
            else prefs[Keys.loggedInUserId] = id
        }
    }

    /** Snabb-helper för logout. */
    suspend fun logout() = setLoggedInUserId(null)

    /** Spara hela utkastet atomärt. Kallas från VM vid varje förändring. */
    suspend fun saveDraft(age: String, hasDL: Boolean, gender: String, phone: String, email: String) {
        dataStore.edit {
            it[Keys.draftAge] = age
            it[Keys.draftHasDL] = hasDL
            it[Keys.draftGender] = gender
            it[Keys.draftPhone] = phone
            it[Keys.draftEmail] = email
        }
    }

    /** Rensa utkast – använd t.ex. efter lyckat spara eller vid logout. */
    suspend fun clearDraft() {
        dataStore.edit {
            it.remove(Keys.draftAge)
            it.remove(Keys.draftHasDL)
            it.remove(Keys.draftGender)
            it.remove(Keys.draftPhone)
            it.remove(Keys.draftEmail)
        }
    }

    /** Ofarlig datastruktur för att läsa/visa utkast. */
    data class Draft(
        val age: String,
        val hasDriverLicense: Boolean,
        val gender: String,
        val phone: String,
        val email: String
    )

    /** Flöde av utkast – användbart om man vill lyssna live. */
    fun draftFlow(): Flow<Draft> = dataStore.data.map {
        Draft(
            age = it[Keys.draftAge] ?: "",
            hasDriverLicense = it[Keys.draftHasDL] ?: false,
            gender = it[Keys.draftGender] ?: "",
            phone = it[Keys.draftPhone] ?: "",
            email = it[Keys.draftEmail] ?: ""
        )
    }

    /** Hämta utkast en gång. Perfekt för init i ViewModel. */
    suspend fun getDraftOnce(): Draft = draftFlow().first()
}