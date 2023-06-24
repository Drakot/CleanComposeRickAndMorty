package com.aluengo.cleancomposerickandmorty.core.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument


private const val ROUTE_LIST_CHARACTERS = "listCharacters"
private const val ROUTE_CHARACTER = "character/%s"

private const val ARG_CHARACTER_ID = "characterId"

sealed class NavRoutes(
    val route: String,
    val arguments: List<NamedNavArgument> = emptyList()
) {
    object ListCharacters : NavRoutes(ROUTE_LIST_CHARACTERS)

    object Character : NavRoutes(
        route = String.format(ROUTE_CHARACTER, "{$ARG_CHARACTER_ID}"),
        arguments = listOf(navArgument(ARG_CHARACTER_ID) {
            type = NavType.IntType
        })
    ) {

        fun routeForCharacter(input: CharacterItemInput? = null) =
            String.format(ROUTE_CHARACTER, input?.id)

        fun fromEntry(entry: NavBackStackEntry): CharacterItemInput {
            val id = entry.arguments?.getString(ARG_CHARACTER_ID) ?.toIntOrNull() ?: 0
            val idVal = if (id == 0) {
                null
            } else {
                id
            }
            return CharacterItemInput(idVal)
        }
    }
}