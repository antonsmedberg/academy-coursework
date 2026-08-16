package com.example.mobilt_java24_anton_smedberg_lifecycle_v5.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilt_java24_anton_smedberg_lifecycle_v5.data.prefs.SessionPrefs
import com.example.mobilt_java24_anton_smedberg_lifecycle_v5.data.repo.AuthRepository
import com.example.mobilt_java24_anton_smedberg_lifecycle_v5.data.util.Validators
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val auth: AuthRepository,
    private val prefs: SessionPrefs
) : ViewModel() {

    data class UiState(
        val username: String = "",
        val password: String = "",
        val error: String? = null,
        val loading: Boolean = false,
        val canSubmit: Boolean = false
    )

    private val _ui = MutableStateFlow(UiState())
    val ui: StateFlow<UiState> = _ui

    fun onUsername(v: String) { update { it.copy(username = v).validate() } }
    fun onPassword(v: String) { update { it.copy(password = v).validate() } }

    fun login(onSuccess: () -> Unit) {
        val s = _ui.value
        if (_ui.value.loading || !_ui.value.canSubmit) return   // dubbelklick-skydd
        update { it.copy(loading = true, error = null) }
        viewModelScope.launch {
            val res = auth.login(s.username.trim(), s.password)
            res.onSuccess { uid ->
                // Sätt loading false före navigation för säkerhets skull
                update { it.copy(loading = false) }
                // Spara autologin
                prefs.setLoggedInUserId(uid)
                onSuccess()
            }.onFailure { e ->
                update { it.copy(error = e.message, loading = false) }
            }
        }
    }

    private fun UiState.validate(): UiState {
        val ok = Validators.username(username.trim()) && Validators.password(password)
        return copy(canSubmit = ok, error = null)
    }

    private fun update(f: (UiState) -> UiState) { _ui.value = f(_ui.value) }
}