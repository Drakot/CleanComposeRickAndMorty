package com.aluengo.cleancomposerickandmorty.listCharacters.presentation


import androidx.annotation.Keep

@Keep
data class ListCharactersUI(
    val results: List<Result>
) {

    @Keep
    data class PageInfo(
        var count: Int = 1,
        var pages: Int = 1,
        var currentPage: Int = 1,
        var lastPage: Boolean = false
    )

    @Keep
    data class Result(
        val id: Int,
        val image: String,
        val name: String,
    )
}