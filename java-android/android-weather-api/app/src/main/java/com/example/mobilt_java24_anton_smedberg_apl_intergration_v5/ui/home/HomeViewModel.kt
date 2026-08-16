package com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.data.Repository
import com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.domain.model.City
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed interface HomeUiState {
    data object Idle : HomeUiState
    data object Loading : HomeUiState
    data class Data(val items: List<City>, val savedKeys: Set<String>) : HomeUiState
    data class Error(val throwable: Throwable) : HomeUiState
}

private fun City.stableKey() = "$name|$latitude|$longitude"

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class HomeViewModel(
    private val repository: Repository,
    // <-- inte 'val': vi behöver inte spara den som property, bara använda i state-byggandet
    isOnlineFlow: Flow<Boolean>
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")

    // Favoriter som stabila nycklar → enkelt att markera bokmärkesikonen
    private val favoriteKeys: StateFlow<Set<String>> =
        repository.observeSaved()
            .map { list -> list.map { it.stableKey() }.toSet() }
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptySet())

    // Putsad query-ström (debounce + trim + distinct)
    private val cleanedQuery: Flow<String> =
        searchQuery
            .debounce(300)
            .map { it.trim() }
            .distinctUntilChanged()

    // Publikt UI-state byggt av: query + online-status + favoriter
    val state: StateFlow<HomeUiState> =
        combine(
            cleanedQuery,
            isOnlineFlow.distinctUntilChanged(),
            favoriteKeys
        ) { q, online, favs -> Triple(q, online, favs) }
            .flatMapLatest { (q, online, favs) ->
                when {
                    q.length < 2   -> flowOf<HomeUiState>(HomeUiState.Idle)
                    !online        -> flowOf<HomeUiState>(HomeUiState.Error(IllegalStateException("Offline")))
                    else           -> flow {
                        emit(HomeUiState.Loading)
                        val items = repository.searchCities(q)
                        emit(HomeUiState.Data(items, favs))
                    }.catch { e -> emit(HomeUiState.Error(e)) }
                }
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HomeUiState.Idle)

    fun updateQuery(newQuery: String) {
        searchQuery.value = newQuery
    }

    fun toggleFavorite(city: City) = viewModelScope.launch {
        val key = city.stableKey()
        if (key in favoriteKeys.value) repository.deleteCity(city)
        else repository.saveCity(city)
        // DB-flödet uppdaterar favoriteKeys/state åt oss.
    }
}