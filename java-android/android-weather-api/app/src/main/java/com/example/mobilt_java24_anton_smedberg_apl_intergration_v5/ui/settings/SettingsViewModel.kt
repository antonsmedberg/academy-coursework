// ui/settings/SettingsViewModel.kt
package com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.util.SettingsStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

data class SettingsUiState(
    val useFahrenheit: Boolean = false,
    val alertsEnabled: Boolean = false
)

/**
 * Håller ihop inställningar (DataStore) och exponerar ett enkelt UI-state.
 * UI:et pratar bara med ViewModel; ViewModel sköter persistens.
 */
class SettingsViewModel(
    private val settings: SettingsStore
) : ViewModel() {

    private val _ui = MutableStateFlow(SettingsUiState())
    val state: StateFlow<SettingsUiState> = _ui.asStateFlow()

    init {
        // Spegla DataStore → UI
        viewModelScope.launch {
            combine(
                settings.useFahrenheitFlow,
                settings.alertsEnabledFlow
            ) { useF, alerts ->
                SettingsUiState(useFahrenheit = useF, alertsEnabled = alerts)
            }.collect { _ui.value = it }
        }
    }

    fun setUnits(useFahrenheit: Boolean) = viewModelScope.launch {
        settings.setUseFahrenheit(useFahrenheit)
    }

    fun setAlerts(enabled: Boolean) = viewModelScope.launch {
        settings.setAlertsEnabled(enabled)
    }
}