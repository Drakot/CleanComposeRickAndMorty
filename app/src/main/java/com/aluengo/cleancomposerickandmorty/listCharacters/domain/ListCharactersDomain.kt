package com.aluengo.cleancomposerickandmorty.listCharacters.domain


import androidx.annotation.Keep

@Keep
data class ListCharactersDomain(
    val info: Info,
    val results: List<Result>
) {
    @Keep
    data class Info(
        val count: Int,
        val next: String?,
        val pages: Int,
        val prev: String?
    )

    @Keep
    data class Result(
        val id: Int,
        val gender: String,
        val image: String,
        val name: String,
        val species: String,
        val status: String,
        val type: String,
        val url: String
    )

}