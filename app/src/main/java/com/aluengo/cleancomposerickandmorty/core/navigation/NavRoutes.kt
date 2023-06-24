package com.aluengo.cleancomposerickandmorty.core.navigation

import androidx.navigation.NamedNavArgument


private const val ROUTE_LIST_CHARACTERS = "listCharacters"

sealed class NavRoutes(
    val route: String,
    val arguments: List<NamedNavArgument> = emptyList()
) {
    object ListCharacters : NavRoutes(ROUTE_LIST_CHARACTERS)
}