package com.example.serviciosapp.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.serviciosapp.data.model.ServiceProvider
import com.example.serviciosapp.ui.components.AvatarPlaceholder
import com.example.serviciosapp.ui.components.PortfolioGrid
import com.example.serviciosapp.ui.components.RatingStars
import com.example.serviciosapp.ui.components.ReviewItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProviderDetailScreen(
    provider: ServiceProvider?,
    onBack: () -> Unit,
    onShowOnMap: (() -> Unit)? = null
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del prestador") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors()
            )
        }
    ) { padding ->
        if (provider == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text("No se encontró el prestador.", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                        AvatarPlaceholder(name = provider.name)
                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(provider.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                            Text("${provider.trade} • ${provider.city}", style = MaterialTheme.typography.titleMedium)
                            RatingStars(rating = provider.rating, size = 16.dp)
                        }
                        if (onShowOnMap != null) {
                            IconButton(onClick = onShowOnMap) {
                                Icon(Icons.Default.Place, contentDescription = "Ver en mapa", tint = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        provider.badges.forEach { badge ->
                            AssistChip(
                                onClick = {},
                                leadingIcon = { Icon(Icons.Default.Verified, contentDescription = null) },
                                label = { Text(badge) }
                            )
                        }
                    }
                }
                item {
                    Text("Sobre el servicio", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text(provider.description, style = MaterialTheme.typography.bodyLarge)
                }
                item {
                    Text("Portafolio", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(8.dp))
                    PortfolioGrid(
                        swatches = provider.portfolioSwatches,
                        modifier = Modifier.fillMaxWidth(),
                        height = 240.dp
                    )
                }
                if (provider.reviews.isNotEmpty()) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Reseñas", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Star, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                Text(
                                    text = String.format("%.1f", provider.rating),
                                    modifier = Modifier.padding(start = 4.dp),
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            provider.reviews.forEach { review ->
                                ReviewItem(review = review, modifier = Modifier.fillMaxWidth())
                            }
                        }
                    }
                }
                item { Divider() }
                item {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        tonalElevation = 3.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text("¿Listo para contactar?", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                            Text("Acción demo: no inicia llamada real.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Button(
                                onClick = { /* Placeholder action */ },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.Call, contentDescription = null)
                                Text("Contactar", modifier = Modifier.padding(start = 8.dp))
                            }
                        }
                    }
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}
