package com.example.mobilt_java24_anton_smedberg_lifecycle_v5.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilt_java24_anton_smedberg_lifecycle_v5.data.model.Profile
import com.example.mobilt_java24_anton_smedberg_lifecycle_v5.data.prefs.SessionPrefs
import com.example.mobilt_java24_anton_smedberg_lifecycle_v5.data.repo.ProfileRepository
import com.example.mobilt_java24_anton_smedberg_lifecycle_v5.data.util.Validators
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class MainViewModel(
    private val prefs: SessionPrefs,
    private val repo: ProfileRepository
) : ViewModel() {

    data class UiState(
        val userId: Long? = null,
        val age: String = "",
        val hasDL: Boolean = false,
        val gender: String = "",
        val phone: String = "",
        val email: String = "",
        val errors: Map<String, String> = emptyMap(),
        val canSubmit: Boolean = false,
        val saved: Profile? = null,
        val message: String? = null,
        val saving: Boolean = false
    )

    private val _ui = MutableStateFlow(UiState())
    val ui: StateFlow<UiState> = _ui.asStateFlow()

    // Engångs-händelse vid lyckat spara. Jag navigerar på denna, aldrig på permanent state.
    private val _savedEvent = MutableSharedFlow<Unit>(replay = 0, extraBufferCapacity = 1)
    val savedEvent: SharedFlow<Unit> = _savedEvent

    init {
        viewModelScope.launch {
            prefs.loggedInUserIdFlow
                .distinctUntilChanged()
                .collect { id ->
                    update { it.copy(userId = id) }
                    if (id != null) loadSaved(id)
                }
        }
        viewModelScope.launch {
            runCatching { prefs.getDraftOnce() }
                .onSuccess { d ->
                    update {
                        it.copy(
                            age = d.age,
                            hasDL = d.hasDriverLicense,
                            gender = d.gender,
                            phone = d.phone,
                            email = d.email
                        ).validate()
                    }
                }
        }
    }

    private suspend fun loadSaved(id: Long) {
        runCatching { repo.get(id) }.onSuccess { saved ->
            if (saved != null) {
                update {
                    it.copy(
                        age = saved.age.toString(),
                        hasDL = saved.hasDriverLicense,
                        gender = saved.gender,
                        phone = saved.phone,
                        email = saved.email,
                        saved = saved
                    ).validate()
                }
            }
        }
    }

    fun onAge(v: String)    = change { it.copy(age = v) }
    fun onHasDL(v: Boolean) = change { it.copy(hasDL = v) }
    fun onGender(v: String) = change { it.copy(gender = v) }
    fun onPhone(v: String)  = change { it.copy(phone = v) }
    fun onEmail(v: String)  = change { it.copy(email = v) }

    private fun change(f: (UiState) -> UiState) {
        update { f(it).validate() }
        viewModelScope.launch {
            val s = _ui.value
            runCatching { prefs.saveDraft(s.age, s.hasDL, s.gender, s.phone, s.email) }
        }
    }

    fun submit() {
        val s = _ui.value
        val uid = s.userId ?: return
        if (!s.canSubmit || s.saving) return
        val ageInt = s.age.toIntOrNull() ?: return

        update { it.copy(saving = true, message = null) }

        viewModelScope.launch {
            runCatching {
                val newProfile = Profile(
                    userId = uid,
                    age = ageInt,
                    hasDriverLicense = s.hasDL,
                    gender = s.gender,
                    phone = s.phone,
                    email = s.email
                )
                repo.save(newProfile)
                runCatching { prefs.clearDraft() }
                newProfile
            }.onSuccess { saved ->
                update { it.copy(saving = false, saved = saved, message = "Sparat!") }
                _savedEvent.tryEmit(Unit) // endast här
            }.onFailure {
                update { it.copy(saving = false, message = "Kunde inte spara just nu") }
            }
        }
    }

    fun messageShown() = update { it.copy(message = null) }

    private fun UiState.validate(): UiState {
        val errs = buildMap {
            if (!Validators.age(age)) put("age", "Ogiltig ålder (≥ 13)")
            if (gender.isBlank()) put("gender", "Välj ett alternativ")
            if (!Validators.phone(phone)) put("phone", "Ogiltigt telefonformat")
            if (!Validators.email(email)) put("email", "Ogiltig e-post")
        }
        return copy(errors = errs, canSubmit = errs.isEmpty())
    }

    private fun update(f: (UiState) -> UiState) {
        _ui.value = f(_ui.value)
    }
}