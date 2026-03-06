package com.example.serviciosapp.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.padding
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavController
import com.example.serviciosapp.AppContainer
import com.example.serviciosapp.data.session.UserRole
import com.example.serviciosapp.ui.auth.AuthViewModel
import com.example.serviciosapp.ui.auth.LoginScreen
import com.example.serviciosapp.ui.auth.RegisterScreen
import com.example.serviciosapp.ui.detail.ProviderDetailScreen
import com.example.serviciosapp.ui.favorites.FavoritesScreen
import com.example.serviciosapp.ui.map.MapScreen
import com.example.serviciosapp.ui.map.MapViewModel
import com.example.serviciosapp.ui.map.SearchScreen
import com.example.serviciosapp.ui.profile.ProfileScreen
import com.example.serviciosapp.ui.splash.SplashGate
import androidx.compose.material.icons.filled.Place
import kotlinx.coroutines.flow.StateFlow
import androidx.compose.ui.Modifier

@Composable
fun ServiciosNavHost(appContainer: AppContainer) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.Splash
    ) {
        composable(NavRoutes.Splash) {
            SplashGate(
                sessionManager = appContainer.sessionManager,
                onAuthRequired = {
                    navController.navigate(NavRoutes.AuthGraph) { popUpTo(NavRoutes.Splash) { inclusive = true } }
                },
                onSessionActive = {
                    navController.navigate(NavRoutes.MainGraph) { popUpTo(NavRoutes.Splash) { inclusive = true } }
                }
            )
        }

        navigation(startDestination = NavRoutes.Login, route = NavRoutes.AuthGraph) {
            composable(NavRoutes.Login) {
                val authViewModel: AuthViewModel = viewModel(factory = authViewModelFactory(appContainer))
                LoginScreen(
                    viewModel = authViewModel,
                    onNavigateToRegister = { navController.navigate(NavRoutes.Register) },
                    onLoginSuccess = {
                        navController.navigate(NavRoutes.MainGraph) {
                            popUpTo(NavRoutes.AuthGraph) { inclusive = true }
                        }
                    },
                    onDemoConsumer = {
                        authViewModel.demoLogin(UserRole.CONSUMER) {
                            navController.navigate(NavRoutes.MainGraph) {
                                popUpTo(NavRoutes.AuthGraph) { inclusive = true }
                            }
                        }
                    },
                    onDemoProvider = {
                        authViewModel.demoLogin(UserRole.PROVIDER) {
                            navController.navigate(NavRoutes.MainGraph) {
                                popUpTo(NavRoutes.AuthGraph) { inclusive = true }
                            }
                        }
                    }
                )
            }
            composable(NavRoutes.Register) {
                val authViewModel: AuthViewModel = viewModel(factory = authViewModelFactory(appContainer))
                RegisterScreen(
                    viewModel = authViewModel,
                    onNavigateToLogin = {
                        navController.popBackStack()
                    },
                    onRegisterSuccess = {
                        navController.navigate(NavRoutes.MainGraph) {
                            popUpTo(NavRoutes.AuthGraph) { inclusive = true }
                        }
                    }
                )
            }
        }

        composable(NavRoutes.MainGraph) {
            MainShell(appContainer = appContainer, rootNavController = navController)
        }
    }
}

private fun authViewModelFactory(appContainer: AppContainer): ViewModelProvider.Factory =
    viewModelFactory {
        initializer {
            AuthViewModel(appContainer.authRepository, appContainer.sessionManager)
        }
    }

private fun mapViewModelFactory(appContainer: AppContainer): ViewModelProvider.Factory =
    viewModelFactory {
        initializer {
            MapViewModel(appContainer.sampleData, appContainer.favoritesRepository)
        }
    }

private data class BottomDestination(
    val route: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@Composable
private fun MainShell(appContainer: AppContainer, rootNavController: NavController) {
    val bottomNavController = rememberNavController()
    val mapViewModel: MapViewModel = viewModel(factory = mapViewModelFactory(appContainer))

    val bottomDestinations = listOf(
        BottomDestination(NavRoutes.Map, "Mapa", Icons.Default.Place),
        BottomDestination(NavRoutes.Search, "Buscar", Icons.Default.Search),
        BottomDestination(NavRoutes.Favorites, "Favoritos", Icons.Default.FavoriteBorder),
        BottomDestination(NavRoutes.Profile, "Perfil", Icons.Default.Person)
    )

    val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            if (currentDestination.shouldShowBottomBar()) {
                NavigationBar {
                    bottomDestinations.forEach { dest ->
                        NavigationBarItem(
                            selected = currentDestination.isOnDestination(dest.route),
                            onClick = {
                                bottomNavController.navigate(dest.route) {
                                    popUpTo(bottomNavController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(dest.icon, contentDescription = dest.label) },
                            label = { Text(dest.label) }
                        )
                    }
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = bottomNavController,
            startDestination = NavRoutes.Map,
            modifier = Modifier.padding(padding)
        ) {
            composable(NavRoutes.Map) {
                MapScreen(
                    viewModel = mapViewModel,
                    onProviderClick = { providerId ->
                        bottomNavController.navigate("main/provider/$providerId")
                    }
                )
            }
            composable(NavRoutes.Search) {
                SearchScreen(
                    viewModel = mapViewModel,
                    onProviderClick = { providerId ->
                        bottomNavController.navigate("main/provider/$providerId")
                    }
                )
            }
            composable(NavRoutes.Favorites) {
                FavoritesScreen(
                    viewModel = mapViewModel,
                    onProviderClick = { providerId ->
                        bottomNavController.navigate("main/provider/$providerId")
                    }
                )
            }
            composable(
                NavRoutes.ProviderDetail,
                arguments = listOf(navArgument(NavRoutes.ProviderDetailArg) { type = NavType.StringType })
            ) { backStackEntry ->
                val providerId = backStackEntry.arguments?.getString(NavRoutes.ProviderDetailArg) ?: return@composable
                val provider = appContainer.sampleData.providers.find { it.id == providerId }
                ProviderDetailScreen(
                    provider = provider,
                    onBack = { bottomNavController.popBackStack() },
                    onShowOnMap = {
                        mapViewModel.selectProvider(providerId)
                        bottomNavController.navigate(NavRoutes.Map) {
                            popUpTo(bottomNavController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
            composable(NavRoutes.Profile) {
                ProfileScreen(
                    sessionManager = appContainer.sessionManager,
                    onLogout = {
                        rootNavController.navigate(NavRoutes.AuthGraph) {
                            popUpTo(NavRoutes.MainGraph) { inclusive = true }
                        }
                    },
                    onBack = { bottomNavController.popBackStack() }
                )
            }
        }
    }
}

private fun NavDestination?.isOnDestination(route: String): Boolean =
    this?.hierarchy?.any { it.route == route } == true

private fun NavDestination?.shouldShowBottomBar(): Boolean =
    this?.route in listOf(NavRoutes.Map, NavRoutes.Search, NavRoutes.Favorites, NavRoutes.Profile)
