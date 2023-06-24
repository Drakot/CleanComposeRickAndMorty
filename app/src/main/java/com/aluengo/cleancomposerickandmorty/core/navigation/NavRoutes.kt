package com.aluengo.cleancomposerickandmorty.core.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument


private const val ROUTE_LIST_CHARACTERS = "listCharacters"
private const val ROUTE_CHARACTER = "character/%s1/%s2"

private const val ARG_CHARACTER_ID = "characterId"
private const val ARG_CHARACTER_NAME = "characterName"

sealed class NavRoutes(
    val route: String,
    val arguments: List<NamedNavArgument> = emptyList()
) {
    object ListCharacters : NavRoutes(ROUTE_LIST_CHARACTERS)

    object Character : NavRoutes(
        route = String.format(ROUTE_CHARACTER, "{$ARG_CHARACTER_ID}", "{$ARG_CHARACTER_NAME}"),
        arguments = listOf(navArgument(ARG_CHARACTER_ID) {
            type = NavType.IntType
        }, navArgument(ARG_CHARACTER_NAME) {
            type = NavType.StringType
        })
    ) {

        fun routeForCharacter(input: CharacterItemInput? = null) =
            String.format(ROUTE_CHARACTER, input?.id, input?.name)

        fun fromEntry(entry: NavBackStackEntry): CharacterItemInput {
            val id = entry.arguments?.getString(ARG_CHARACTER_ID)?.toIntOrNull() ?: 0
            val name = entry.arguments?.getString(ARG_CHARACTER_NAME) ?: ""
            val idVal = if (id == 0) {
                null
            } else {
                id
            }
            return CharacterItemInput(idVal, name)
        }
    }
}