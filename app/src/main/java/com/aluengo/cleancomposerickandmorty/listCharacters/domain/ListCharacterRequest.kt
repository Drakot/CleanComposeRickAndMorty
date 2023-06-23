package com.aluengo.cleancomposerickandmorty.listCharacters.domain

data class ListCharacterRequest(val filter: String? = null, val page: Int = 1)