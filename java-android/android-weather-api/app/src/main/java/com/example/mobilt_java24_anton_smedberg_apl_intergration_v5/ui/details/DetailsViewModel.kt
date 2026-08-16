// ui/details/DetailsViewModel.kt
package com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.data.Repository
import com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.domain.model.City
import com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.domain.model.WeatherSnapshot
import com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.util.SettingsStore
import com.example.timelib.TempUnit
import com.example.timelib.TimeUtils
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

sealed interface DetailsUiState {
    data object Loading : DetailsUiState
    data class Data(val headerText: String, val days: List<DayRow>) : DetailsUiState
    data class Error(val message: String) : DetailsUiState
}

class DetailsViewModel(
    private val repo: Repository,
    private val settings: SettingsStore
) : ViewModel() {

    private val _state = MutableStateFlow<DetailsUiState>(DetailsUiState.Loading)
    val state: StateFlow<DetailsUiState> = _state

    private val snapshot = MutableStateFlow<WeatherSnapshot?>(null)
    private var combineJob: Job? = null

    fun load(city: City) {
        _state.value = DetailsUiState.Loading

        // Hämta & cacha en snapshot för vald stad
        viewModelScope.launch {
            try {
                snapshot.value = repo.getForecastFor(city)
            } catch (_: Throwable) {
                _state.value = DetailsUiState.Error(
                    if (Locale.getDefault().language == "sv") "Kunde inte hämta prognos"
                    else "Could not load forecast"
                )
            }
        }

        // Bygg UI varje gång enheten ändras — från samma snapshot
        combineJob?.cancel()
        combineJob = viewModelScope.launch {
            combine(
                snapshot.filterNotNull(),
                settings.useFahrenheitFlow.distinctUntilChanged()
            ) { snap, useFahrenheit ->
                val unit = if (useFahrenheit) TempUnit.F else TempUnit.C
                DetailsUiState.Data(
                    headerText = formatHeader(snap, unit),
                    days = buildDayRows(snap, unit)
                )
            }.collect { _state.value = it }
        }
    }

    private fun formatHeader(snap: WeatherSnapshot, unit: TempUnit): String {
        val tempNow = TimeUtils.temp(snap.currentTemp, unit, decimals = 1, withUnit = true)
        val rain = snap.nextHourRainProb
        val sv = Locale.getDefault().language == "sv"
        return if (sv) {
            if (rain > 0) "Nu: $tempNow • Nästa timme: $rain% regn" else "Nu: $tempNow"
        } else {
            if (rain > 0) "Now: $tempNow • Next hour: $rain% rain" else "Now: $tempNow"
        }
    }

    private fun buildDayRows(snap: WeatherSnapshot, unit: TempUnit): List<DayRow> {
        val loc = Locale.getDefault()
        val today = LocalDate.now()
        val dayFmt = DateTimeFormatter.ofPattern("EEEE d MMM", loc)

        fun emojiFor(code: Int) = when (code) {
            0 -> "☀️"
            1, 2 -> "🌤️"
            3 -> "☁️"
            in 45..48 -> "🌫️"
            in 51..67 -> "🌦️"
            in 71..86 -> "🌨️"
            in 95..99 -> "⛈️"
            else -> "🌥️"
        }

        return snap.days.mapNotNull { d ->
            val date = runCatching { LocalDate.parse(d.dateIso) }.getOrNull() ?: return@mapNotNull null
            val label = if (date == today) {
                if (loc.language == "sv") "Idag" else "Today"
            } else {
                dayFmt.format(date).replaceFirstChar { if (it.isLowerCase()) it.titlecase(loc) else it.toString() }
            }

            DayRow(
                epochDay = date.toEpochDay(),
                dayLabel = label,
                emoji = emojiFor(d.weatherCode),
                condition = codeToText(d.weatherCode, loc),
                highTempLabel = TimeUtils.hiArrow(d.tMax, unit),
                lowTempLabel = TimeUtils.loArrow(d.tMin, unit),
                rainChancePct = d.rainProbMax
            )
        }
    }

    private fun codeToText(code: Int, loc: Locale): String = when (code) {
        0 -> if (loc.language == "sv") "Klart" else "Clear"
        1, 2 -> if (loc.language == "sv") "Mest klart" else "Mostly clear"
        3 -> if (loc.language == "sv") "Mulet" else "Overcast"
        in 45..48 -> if (loc.language == "sv") "Dimma" else "Fog"
        in 51..67 -> if (loc.language == "sv") "Dugg/Regn" else "Drizzle/Rain"
        in 71..86 -> if (loc.language == "sv") "Snö" else "Snow"
        in 95..99 -> if (loc.language == "sv") "Åska" else "Thunderstorm"
        else -> if (loc.language == "sv") "Växlande moln" else "Partly cloudy"
    }
}