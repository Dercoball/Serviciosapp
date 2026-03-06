package com.example.serviciosapp.ui.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.serviciosapp.ui.components.ProviderCard
import com.example.serviciosapp.ui.map.MapViewModel

@Composable
fun FavoritesScreen(
    viewModel: MapViewModel,
    onProviderClick: (String) -> Unit
) {
    val favorites by viewModel.favoritesIds.collectAsState()
    val providers = viewModel.providers.filter { favorites.contains(it.provider.id) }
    if (providers.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Sin favoritos aún", style = MaterialTheme.typography.headlineSmall)
            Text(
                "Marca prestadores desde el mapa o la búsqueda para verlos aquí.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(providers) { item ->
                ProviderCard(
                    provider = item.provider,
                    distanceKm = item.distanceKm,
                    highlighted = false,
                    onClick = {
                        viewModel.selectProvider(item.provider.id)
                        onProviderClick(item.provider.id)
                    },
                    onViewProfile = { onProviderClick(item.provider.id) },
                    isFavorite = true,
                    onToggleFavorite = { viewModel.toggleFavorite(item.provider.id) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
