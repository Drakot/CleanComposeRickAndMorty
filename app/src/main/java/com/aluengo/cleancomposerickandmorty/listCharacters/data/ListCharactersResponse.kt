package com.aluengo.cleancomposerickandmorty.listCharacters.data


import androidx.annotation.Keep
import com.aluengo.cleancomposerickandmorty.listCharacters.domain.ListCharactersDomain

@Keep
data class ListCharactersResponse(
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
        val created: String,
        val episode: List<String>,
        val gender: String,
        val id: Int,
        val image: String,
        val location: Location,
        val name: String,
        val origin: Origin,
        val species: String,
        val status: String,
        val type: String,
        val url: String
    ) {
        @Keep
        data class Location(
            val name: String,
            val url: String
        )

        @Keep
        data class Origin(
            val name: String,
            val url: String
        )
    }

    fun toDomain(): ListCharactersDomain {
        return ListCharactersDomain(
            ListCharactersDomain.Info(info.count, info.next, info.pages, info.prev),
            results = results.map {
                ListCharactersDomain.Result(
                    it.id,
                    it.gender,
                    it.image,
                    it.name,
                    it.species,
                    it.status,
                    it.type,
                    it.url
                )
            }
        )
    }

}