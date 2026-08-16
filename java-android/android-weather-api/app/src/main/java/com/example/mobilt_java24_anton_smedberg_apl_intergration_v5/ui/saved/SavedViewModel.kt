// ui/saved/SavedViewModel.kt
package com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.ui.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.data.Repository
import com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.domain.model.City
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SavedViewModel(private val repo: Repository) : ViewModel() {

    /** Favoriter direkt från Room (en enda sanning) */
    val saved: StateFlow<List<City>> =
        repo.observeSaved().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun delete(city: City) = viewModelScope.launch {
        repo.deleteCity(city)
        // Flödet uppdaterar UI automatiskt
    }
}