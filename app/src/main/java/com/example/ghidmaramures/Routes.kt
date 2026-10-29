package com.example.ghidmaramures

import kotlinx.serialization.Serializable

@Serializable data object Login
@Serializable data object CategoriesGraph
@Serializable data object PlaceList
@Serializable data class PlaceDetail(val id: Int)
@Serializable data object Favorites
@Serializable data object About
