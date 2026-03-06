package com.example.serviciosapp.ui.map

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serviciosapp.data.favorites.FavoritesRepository
import com.example.serviciosapp.data.model.ServiceProvider
import com.example.serviciosapp.data.sample.SampleData
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.round
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class SortType { TOP_RATED, NEAREST }
enum class SheetTarget { COLLAPSED, HALF, EXPANDED }

data class ProviderWithDistance(
    val provider: ServiceProvider,
    val distanceKm: Double
)

class MapViewModel(
    sampleData: SampleData,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    private val demoUserLocation = Coord(19.4326, -99.1332) // CDMX centro (demo)

    val categories: List<String> = sampleData.categories
    private val allProviders: List<ServiceProvider> = sampleData.providers

    var searchQuery by mutableStateOf("")
        private set
    var selectedCategory by mutableStateOf<String?>(null)
        private set
    var sortType by mutableStateOf(SortType.TOP_RATED)
        private set
    var selectedProvider by mutableStateOf<ProviderWithDistance?>(null)
        private set
    var providers by mutableStateOf<List<ProviderWithDistance>>(emptyList())
        private set
    var userLocation by mutableStateOf(demoUserLocation)
        private set
    var sheetTarget by mutableStateOf(SheetTarget.HALF)
        private set

    private val _favoritesIds = favoritesRepository.favoritesFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptySet())
    val favoritesIds: StateFlow<Set<String>> = _favoritesIds

    init {
        refresh()
    }

    fun onQueryChange(value: String) {
        searchQuery = value
        refresh(selectedProvider?.provider?.id)
    }

    fun onCategoryToggle(category: String) {
        selectedCategory = if (selectedCategory == category) null else category
        refresh(selectedProvider?.provider?.id)
    }

    fun onSortChange(sort: SortType) {
        sortType = sort
        refresh(selectedProvider?.provider?.id)
    }

    fun selectProvider(id: String?) {
        selectedProvider = providers.find { it.provider.id == id }
        if (id != null) sheetTarget = SheetTarget.HALF
    }

    fun centerOnUser() {
        sheetTarget = SheetTarget.HALF
        refresh(selectedProvider?.provider?.id)
    }

    fun updateSheet(target: SheetTarget) {
        sheetTarget = target
    }

    fun toggleFavorite(id: String) {
        viewModelScope.launch {
            favoritesRepository.toggleFavorite(id)
        }
    }

    fun isFavorite(id: String): Boolean = favoritesIds.value.contains(id)

    private fun refresh(keepSelectedId: String? = null) {
        val mapped = allProviders
            .map { provider ->
                ProviderWithDistance(
                    provider = provider,
                    distanceKm = distanceInKm(
                        provider.latitude,
                        provider.longitude,
                        userLocation.lat,
                        userLocation.lon
                    )
                )
            }
            .filter { matchesFilters(it.provider) }
            .sortedWith(
                when (sortType) {
                    SortType.TOP_RATED -> compareByDescending<ProviderWithDistance> { it.provider.rating }
                        .thenBy { it.distanceKm }
                    SortType.NEAREST -> compareBy<ProviderWithDistance> { it.distanceKm }
                        .thenByDescending { it.provider.rating }
                }
            )

        providers = mapped
        selectedProvider = keepSelectedId?.let { id -> mapped.find { it.provider.id == id } }
    }

    private fun matchesFilters(provider: ServiceProvider): Boolean {
        val matchesCategory = selectedCategory?.let { provider.category.equals(it, ignoreCase = true) } ?: true
        val query = searchQuery.trim()
        val matchesQuery = query.isBlank() || provider.name.contains(query, ignoreCase = true) ||
            provider.trade.contains(query, ignoreCase = true) ||
            provider.city.contains(query, ignoreCase = true)
        return matchesCategory && matchesQuery
    }

    private fun distanceInKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val earthRadius = 6371.0
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2).pow(2.0) + cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) * sin(dLon / 2).pow(2.0)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return (earthRadius * c * 10.0).roundTo1Decimal()
    }
}

private fun Double.roundTo1Decimal(): Double = kotlin.math.round(this * 10) / 10.0

data class Coord(val lat: Double, val lon: Double)
