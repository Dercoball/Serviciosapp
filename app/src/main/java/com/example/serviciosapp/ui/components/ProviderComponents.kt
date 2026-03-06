package com.example.serviciosapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.serviciosapp.data.model.Review
import com.example.serviciosapp.data.model.ServiceProvider
import kotlin.math.roundToInt

@Composable
fun UberTopSearchBar(
    query: String,
    placeholder: String = "¿Qué servicio necesitas?",
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onFilterClick: (() -> Unit)? = null
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(28.dp),
        tonalElevation = 4.dp,
        shadowElevation = 10.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.size(12.dp))
            val selectionColors = TextSelectionColors(
                handleColor = MaterialTheme.colorScheme.primary,
                backgroundColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
            )
            CompositionLocalProvider(LocalTextSelectionColors provides selectionColors) {
                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    singleLine = true,
                    decorationBox = { inner ->
                        if (query.isBlank()) {
                            Text(placeholder, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        inner()
                    },
                    modifier = Modifier.weight(1f)
                )
            }
            if (onFilterClick != null) {
                IconButton(onClick = onFilterClick) {
                    Icon(Icons.Default.FilterList, contentDescription = "Filtros")
                }
            }
        }
    }
}

@Composable
fun RatingStars(
    rating: Double,
    size: Dp = 16.dp,
    color: Color = MaterialTheme.colorScheme.primary
) {
    val rounded = rating.roundToInt().coerceIn(0, 5)
    Row(horizontalArrangement = Arrangement.spacedBy(2.dp), verticalAlignment = Alignment.CenterVertically) {
        repeat(5) { index ->
            val alpha = if (index < rounded) 1f else 0.25f
            Icon(
                Icons.Filled.Star,
                contentDescription = null,
                tint = color.copy(alpha = alpha),
                modifier = Modifier.size(size)
            )
        }
        Text(
            text = String.format("%.1f", rating),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}

@Composable
fun ProviderCard(
    provider: ServiceProvider,
    distanceKm: Double,
    highlighted: Boolean,
    onClick: () -> Unit,
    onViewProfile: () -> Unit,
    isFavorite: Boolean = false,
    onToggleFavorite: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val cardColors = if (highlighted) {
        CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    } else {
        CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    }
    Card(
        modifier = modifier,
        onClick = onClick,
        colors = cardColors,
        elevation = CardDefaults.cardElevation(defaultElevation = if (highlighted) 8.dp else 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AvatarPlaceholder(name = provider.name)
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(provider.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    if (provider.badges.isNotEmpty()) {
                        Text(
                            provider.badges.first(),
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp))
                                .padding(horizontal = 8.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                Text("${provider.trade} • ${provider.city}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                RatingStars(rating = provider.rating, size = 14.dp)
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                    Text("${distanceKm} km", style = MaterialTheme.typography.labelMedium)
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = onViewProfile, modifier = Modifier.weight(1f)) {
                        Text("Ver perfil")
                    }
                    if (onToggleFavorite != null) {
                        IconButton(onClick = onToggleFavorite) {
                            if (isFavorite) {
                                Icon(Icons.Filled.Favorite, contentDescription = "Quitar de favoritos", tint = MaterialTheme.colorScheme.primary)
                            } else {
                                Icon(Icons.Outlined.FavoriteBorder, contentDescription = "Agregar a favoritos", tint = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AvatarPlaceholder(name: String, modifier: Modifier = Modifier) {
    val initials = remember(name) {
        name.split(" ").take(2).mapNotNull { it.firstOrNull()?.uppercaseChar() }.joinToString("")
    }
    val bg = remember(name) {
        val hash = name.hashCode()
        val r = (hash shr 16 and 0xFF) / 255f
        val g = (hash shr 8 and 0xFF) / 255f
        val b = (hash and 0xFF) / 255f
        Color(r, g, b, alpha = 0.9f)
    }
    Box(
        modifier = modifier
            .size(56.dp)
            .clip(CircleShape)
            .background(bg.copy(alpha = 0.15f))
            .border(1.dp, bg, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(initials, color = bg, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun ReviewItem(review: Review, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text(review.author, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                RatingStars(rating = review.rating, size = 12.dp, color = MaterialTheme.colorScheme.primary)
            }
            Text(review.comment, style = MaterialTheme.typography.bodyMedium, maxLines = 3, overflow = TextOverflow.Ellipsis)
            Text(review.relativeTime, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun PortfolioGrid(
    swatches: List<Long>,
    modifier: Modifier = Modifier,
    height: Dp = 220.dp
) {
    val colors = swatches.ifEmpty { listOf(0xFFE0E7FF, 0xFFD1FAE5, 0xFFFEE2E2) }
    androidx.compose.foundation.lazy.grid.LazyVerticalGrid(
        modifier = modifier.height(height),
        columns = androidx.compose.foundation.lazy.grid.GridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(colors.size) { index ->
            val color = Color(colors[index])
            Box(
                modifier = Modifier
                    .height(90.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(color.copy(alpha = 0.85f))
            )
        }
    }
}
