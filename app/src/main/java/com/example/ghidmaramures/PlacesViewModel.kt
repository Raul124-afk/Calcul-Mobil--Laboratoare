package com.example.ghidmaramures

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

// ViewModel de Activity: starea este comună tuturor tab-urilor.
class PlacesViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    val selectedCategory = savedStateHandle.getStateFlow("category", "ALL")
    val favoriteIds = savedStateHandle.getStateFlow("favorites", intArrayOf())

    fun selectCategory(category: Category?) {
        savedStateHandle["category"] = category?.name ?: "ALL"
    }

    fun toggleFavorite(id: Int) {
        if (PlacesRepository.findById(id) == null) return
        val current = favoriteIds.value.toSet()
        savedStateHandle["favorites"] =
            (if (id in current) current - id else current + id).toIntArray()
    }

    fun removeFavorite(id: Int) {
        savedStateHandle["favorites"] = favoriteIds.value.filterNot { it == id }.toIntArray()
    }
}
