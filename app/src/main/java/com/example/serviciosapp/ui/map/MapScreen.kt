package com.example.serviciosapp.ui.map

import android.view.ViewGroup
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.unit.dp
import com.example.serviciosapp.ui.components.ProviderCard
import com.example.serviciosapp.ui.components.UberTopSearchBar
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import android.preference.PreferenceManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    viewModel: MapViewModel,
    onProviderClick: (String) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var mapView by remember { mutableStateOf<MapView?>(null) }
    var mapReady by remember { mutableStateOf(false) }
    val providers = viewModel.providers
    val selected = viewModel.selectedProvider
    val favorites by viewModel.favoritesIds.collectAsState()

    val sheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.PartiallyExpanded,
        skipHiddenState = false
    )
    val scaffoldState = rememberBottomSheetScaffoldState(sheetState)

    LaunchedEffect(Unit) {
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context))
        Configuration.getInstance().userAgentValue = context.packageName
    }

    // Scroll al item seleccionado en la lista
    val listState = rememberLazyListState()
    LaunchedEffect(selected?.provider?.id) {
        val idx = providers.indexOfFirst { it.provider.id == selected?.provider?.id }
        if (idx >= 0) listState.animateScrollToItem(idx)
    }

    LaunchedEffect(mapReady, selected?.provider?.id) {
        if (mapReady && selected != null) {
            mapView?.controller?.setZoom(15.5)
            mapView?.controller?.animateTo(GeoPoint(selected.provider.latitude, selected.provider.longitude))
            scaffoldState.bottomSheetState.partialExpand()
        }
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 200.dp,
        sheetContainerColor = MaterialTheme.colorScheme.surface,
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                UberTopSearchBar(
                    query = viewModel.searchQuery,
                    onQueryChange = viewModel::onQueryChange,
                    modifier = Modifier.fillMaxWidth(),
                    onFilterClick = {
                        val next = if (viewModel.sortType == SortType.TOP_RATED) SortType.NEAREST else SortType.TOP_RATED
                        viewModel.onSortChange(next)
                    }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(viewModel.categories.size) { index ->
                            val category = viewModel.categories[index]
                            FilterChip(
                                selected = viewModel.selectedCategory == category,
                                onClick = { viewModel.onCategoryToggle(category) },
                                label = { Text(category) }
                            )
                        }
                    }
                    IconButton(onClick = {
                        val next = if (viewModel.sortType == SortType.TOP_RATED) SortType.NEAREST else SortType.TOP_RATED
                        viewModel.onSortChange(next)
                    }) {
                        Icon(Icons.Default.FilterList, contentDescription = "Ordenar")
                    }
                }

                Text(
                    text = if (viewModel.sortType == SortType.TOP_RATED) "Orden: Mejores calificados" else "Orden: Más cercanos",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                LazyColumn(
                    state = listState,
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    itemsIndexed(providers) { index, item ->
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn() + slideInVertically(
                                initialOffsetY = { it / 2 },
                                animationSpec = tween(250 + index * 40)
                            ),
                            exit = fadeOut()
                        ) {
                            ProviderCard(
                                provider = item.provider,
                                distanceKm = item.distanceKm,
                                highlighted = selected?.provider?.id == item.provider.id,
                                onClick = {
                                    viewModel.selectProvider(item.provider.id)
                                    mapView?.controller?.animateTo(
                                        GeoPoint(item.provider.latitude, item.provider.longitude)
                                    )
                                },
                                onViewProfile = { onProviderClick(item.provider.id) },
                                isFavorite = favorites.contains(item.provider.id),
                                onToggleFavorite = { viewModel.toggleFavorite(item.provider.id) }
                            )
                        }
                    }
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { ctx ->
                    MapView(ctx).apply {
                        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                        setTileSource(TileSourceFactory.MAPNIK)
                        setMultiTouchControls(true)
                        controller.setZoom(14.5)
                        controller.setCenter(GeoPoint(viewModel.userLocation.lat, viewModel.userLocation.lon))
                        onResume()
                        mapView = this
                        mapReady = true
                        refreshMarkers(this, providers, selected?.provider?.id, viewModel)
                    }
                },
                update = { map ->
                    refreshMarkers(map, providers, selected?.provider?.id, viewModel)
                }
            )

            IconButton(
                onClick = {
                    viewModel.centerOnUser()
                    mapView?.controller?.animateTo(
                        GeoPoint(viewModel.userLocation.lat, viewModel.userLocation.lon)
                    )
                    coroutineScope.launch { sheetState.partialExpand() }
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .size(52.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MyLocation,
                    contentDescription = "Centrar en mi zona",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(horizontal = 16.dp, vertical = 18.dp)
            ) {
                UberTopSearchBar(
                    query = viewModel.searchQuery,
                    onQueryChange = viewModel::onQueryChange,
                    modifier = Modifier.fillMaxWidth(0.95f)
                )
            }

            Text(
                text = "Modo demo • OSM sin permisos",
                color = Color.White,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                        shape = MaterialTheme.shapes.large
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            mapView?.onPause()
            mapView?.onDetach()
            mapView = null
        }
    }
}

private fun refreshMarkers(
    mapView: MapView,
    providers: List<ProviderWithDistance>,
    selectedId: String?,
    viewModel: MapViewModel
) {
    mapView.overlays.clear()
    providers.forEach { item ->
        val marker = Marker(mapView).apply {
            position = GeoPoint(item.provider.latitude, item.provider.longitude)
            title = item.provider.name
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            if (item.provider.id == selectedId) {
                alpha = 1.0f
            } else {
                alpha = 0.7f
            }
            setOnMarkerClickListener { _, _ ->
                viewModel.selectProvider(item.provider.id)
                true
            }
        }
        mapView.overlays.add(marker)
    }
    mapView.invalidate()
}
