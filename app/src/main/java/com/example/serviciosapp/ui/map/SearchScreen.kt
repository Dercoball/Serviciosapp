package com.example.serviciosapp.ui.map

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.serviciosapp.ui.components.ProviderCard
import com.example.serviciosapp.ui.components.UberTopSearchBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: MapViewModel,
    onProviderClick: (String) -> Unit
) {
    val providers = viewModel.providers
    val favorites by viewModel.favoritesIds.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        UberTopSearchBar(
            query = viewModel.searchQuery,
            onQueryChange = viewModel::onQueryChange,
            modifier = Modifier.fillMaxWidth()
        )
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(viewModel.categories.size) { index ->
                val category = viewModel.categories[index]
                FilterChip(
                    selected = viewModel.selectedCategory == category,
                    onClick = { viewModel.onCategoryToggle(category) },
                    label = { Text(category) }
                )
            }
            item {
                AssistChip(
                    onClick = {
                        val next = if (viewModel.sortType == SortType.TOP_RATED) SortType.NEAREST else SortType.TOP_RATED
                        viewModel.onSortChange(next)
                    },
                    label = { Text(if (viewModel.sortType == SortType.TOP_RATED) "Mejor calificados" else "Más cercanos") }
                )
            }
        }
        Divider()
        Text(
            text = "Resultados",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(providers) { item ->
                ProviderCard(
                    provider = item.provider,
                    distanceKm = item.distanceKm,
                    highlighted = viewModel.selectedProvider?.provider?.id == item.provider.id,
                    onClick = { viewModel.selectProvider(item.provider.id) },
                    onViewProfile = { onProviderClick(item.provider.id) },
                    isFavorite = favorites.contains(item.provider.id),
                    onToggleFavorite = { viewModel.toggleFavorite(item.provider.id) }
                )
            }
            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}
