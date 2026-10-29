package com.example.ghidmaramures

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute

@Composable
fun GuideNavHost(model: PlacesViewModel = viewModel()) {
    val navController = rememberNavController()
    val entry by navController.currentBackStackEntryAsState()
    val categoryName by model.selectedCategory.collectAsStateWithLifecycle()
    val favoriteIds by model.favoriteIds.collectAsStateWithLifecycle()
    val category = Category.entries.find { it.name == categoryName }
    val showBottomBar = entry != null && entry?.destination?.hasRoute<Login>() == false

    Scaffold(
        bottomBar = { if (showBottomBar) GuideBottomBar(navController) }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Login,
            modifier = Modifier.fillMaxSize().padding(padding)
        ) {
            composable<Login> {
                LoginScreen(onLogin = {
                    navController.navigate(CategoriesGraph) {
                        popUpTo<Login> { inclusive = true }
                        launchSingleTop = true
                    }
                })
            }
            navigation<CategoriesGraph>(startDestination = PlaceList) {
                composable<PlaceList> {
                    PlaceListScreen(
                        places = PlacesRepository.filtered(category),
                        selectedCategory = category,
                        onCategorySelected = model::selectCategory,
                        onNavigateToDetail = { id ->
                            navController.navigate(PlaceDetail(id)) { launchSingleTop = true }
                        }
                    )
                }
                composable<PlaceDetail> { detailEntry ->
                    val route = detailEntry.toRoute<PlaceDetail>()
                    PlaceDetailScreen(
                        place = PlacesRepository.findById(route.id),
                        isFavorite = route.id in favoriteIds,
                        onToggleFavorite = { model.toggleFavorite(route.id) },
                        onBack = { navController.popBackStack() }
                    )
                }
            }
            composable<Favorites> {
                FavoritesScreen(
                    places = PlacesRepository.places.filter { it.id in favoriteIds },
                    onRemoveFavorite = model::removeFavorite
                )
            }
            composable<About> { AboutScreen() }
        }
    }
}

// NavController există numai în stratul de navigare, nu în funcțiile Screen.
@Composable
private fun GuideBottomBar(navController: NavHostController) {
    val entry by navController.currentBackStackEntryAsState()
    val destination = entry?.destination
    val categoriesSelected = destination?.hierarchy?.any { it.hasRoute<CategoriesGraph>() } == true
    val favoritesSelected = destination?.hasRoute<Favorites>() == true
    val aboutSelected = destination?.hasRoute<About>() == true

    // Login a fost eliminat: ancora este lista, nu startDestination-ul Login al rădăcinii.
    fun openTab(route: Any, selected: Boolean) {
        if (selected) return // Păstrează și detaliul la reapăsarea pe Categorii.
        navController.navigate(route) {
            popUpTo<PlaceList> { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    NavigationBar(Modifier.testTag("bottom_bar")) {
        NavigationBarItem(
            modifier = Modifier.testTag("tab_categories"),
            selected = categoriesSelected,
            onClick = { openTab(CategoriesGraph, categoriesSelected) },
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text("Categorii") }
        )
        NavigationBarItem(
            modifier = Modifier.testTag("tab_favorites"),
            selected = favoritesSelected,
            onClick = { openTab(Favorites, favoritesSelected) },
            icon = { Icon(Icons.Default.Favorite, contentDescription = null) },
            label = { Text("Favorite") }
        )
        NavigationBarItem(
            modifier = Modifier.testTag("tab_about"),
            selected = aboutSelected,
            onClick = { openTab(About, aboutSelected) },
            icon = { Icon(Icons.Default.Info, contentDescription = null) },
            label = { Text("Despre") }
        )
    }
}
