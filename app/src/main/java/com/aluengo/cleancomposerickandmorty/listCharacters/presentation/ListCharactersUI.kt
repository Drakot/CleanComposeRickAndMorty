package com.aluengo.cleancomposerickandmorty.listCharacters.presentation


import androidx.annotation.Keep
import com.aluengo.cleancomposerickandmorty.core.utils.extractPageNumber
import com.aluengo.cleancomposerickandmorty.listCharacters.domain.ListCharactersDomain

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
        val species: String,
        val origin: String,
        val episodes: String,
        val status: String,
    )


    companion object {
        fun fromDomain(data: ListCharactersDomain): ListCharactersUI {
            return ListCharactersUI(

                results = data.results.map {
                    fromDomain(it)
                }
            )
        }

        fun infoFromDomain(info: ListCharactersDomain.Info?): PageInfo {
            info ?: return PageInfo(1, 1, 1, true)
            return PageInfo(
                info.count,
                info.pages,
                if (info.next == null) info.pages else info.next.extractPageNumber() - 1,
                info.next == null
            )
        }

        fun fromDomain(data: ListCharactersDomain.Result): Result {
            return Result(
                data.id,
                data.image,
                data.name,
                data.species,
                data.origin,
                data.episodes,
                data.status
            )
        }
    }

}