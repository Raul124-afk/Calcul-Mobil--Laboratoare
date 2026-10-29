package com.example.ghidmaramures

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(onLogin: () -> Unit) {
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    Column(
        Modifier.fillMaxSize().imePadding().verticalScroll(rememberScrollState()).padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Ghid Maramureș", style = MaterialTheme.typography.headlineLarge)
        Text("Descoperă locuri. Păstrează favorite.")
        Spacer(Modifier.height(24.dp))
        OutlinedTextField(
            value = username, onValueChange = { username = it },
            label = { Text("Utilizator") }, singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("username")
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = password, onValueChange = { password = it },
            label = { Text("Parolă") }, singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth().testTag("password")
        )
        Spacer(Modifier.height(20.dp))
        Button(
            onClick = onLogin,
            enabled = username.isNotBlank() && password.isNotBlank(),
            modifier = Modifier.fillMaxWidth().testTag("login_button")
        ) { Text("Intră") }
        Text("Autentificare demonstrativă: poți folosi orice nume și parolă completate.",
            style = MaterialTheme.typography.bodySmall)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceListScreen(
    places: List<Place>,
    selectedCategory: Category?,
    onCategorySelected: (Category?) -> Unit,
    onNavigateToDetail: (Int) -> Unit
) {
    val listState = rememberLazyListState()
    Column(Modifier.fillMaxSize()) {
        TopAppBar(title = { Text("Obiective turistice") }, windowInsets = WindowInsets(0, 0, 0, 0))
        Row(
            Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedCategory == null,
                onClick = { onCategorySelected(null) }, label = { Text("Toate") },
                modifier = Modifier.testTag("filter_ALL")
            )
            Category.entries.forEach { category ->
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = { onCategorySelected(category) }, label = { Text(category.label) },
                    modifier = Modifier.testTag("filter_${category.name}")
                )
            }
        }
        Text("${places.size} obiective", Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f).testTag("places_list"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(places, key = { it.id }) { place ->
                Card(
                    onClick = { onNavigateToDetail(place.id) },
                    modifier = Modifier.fillMaxWidth().testTag("place_${place.id}")
                ) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(place.name, style = MaterialTheme.typography.titleMedium)
                        Text("${place.category.label} • ${place.city}")
                        Text("Vezi detalii", color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceDetailScreen(place: Place?, isFavorite: Boolean, onToggleFavorite: () -> Unit, onBack: () -> Unit) {
    Column(Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Detalii obiectiv") },
            windowInsets = WindowInsets(0, 0, 0, 0),
            navigationIcon = {
                IconButton(onClick = onBack, modifier = Modifier.testTag("detail_back")) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Înapoi la listă")
                }
            }
        )
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (place == null) {
                Text("Obiectivul nu a fost găsit.")
                Button(onClick = onBack) { Text("Înapoi la listă") }
            } else {
                Text(place.name, style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.testTag("detail_title"))
                Text("ID: ${place.id}")
                Text("Categorie: ${place.category.label}")
                Text("Localitate: ${place.city}")
                Text(place.description, style = MaterialTheme.typography.bodyLarge)
                Button(onClick = onToggleFavorite, modifier = Modifier.testTag("favorite_toggle")) {
                    Text(if (isFavorite) "Elimină din favorite" else "Adaugă la favorite")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(places: List<Place>, onRemoveFavorite: (Int) -> Unit) {
    Column(Modifier.fillMaxSize()) {
        TopAppBar(title = { Text("Favorite") }, windowInsets = WindowInsets(0, 0, 0, 0))
        if (places.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
                Text("Nu ai încă favorite. Deschide un obiectiv din Categorii și apasă «Adaugă la favorite».",
                    modifier = Modifier.testTag("empty_favorites"))
            }
        } else {
            LazyColumn(
                state = rememberLazyListState(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(places, key = { it.id }) { place ->
                    Card(Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(16.dp)) {
                            Text(place.name, style = MaterialTheme.typography.titleMedium)
                            Text("${place.category.label} • ${place.city}")
                            TextButton(onClick = { onRemoveFavorite(place.id) },
                                modifier = Modifier.testTag("remove_${place.id}")) {
                                Text("Elimină din favorite")
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen() {
    Column(Modifier.fillMaxSize()) {
        TopAppBar(title = { Text("Despre aplicație") }, windowInsets = WindowInsets(0, 0, 0, 0))
        Column(
            Modifier.verticalScroll(rememberScrollState()).padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Ghid Maramureș", style = MaterialTheme.typography.headlineMedium)
            Text("Autor: Raul Berinde")
            Text("Versiunea: 1.0")
            Text("Aplicație pentru descoperirea obiectivelor sportive, muzeelor și restaurantelor din Maramureș. Poți filtra lista și marca locurile preferate.")
            Text("Proiect realizat pentru Calcul Mobil, laboratorul 05, cu Kotlin, Jetpack Compose și Navigation Compose (Nav2).")
        }
    }
}
