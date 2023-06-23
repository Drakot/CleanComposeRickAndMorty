package com.aluengo.cleancomposerickandmorty.listCharacters.presentation


import androidx.annotation.Keep
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
    )


    companion object {
        fun fromDomain(data: ListCharactersDomain): ListCharactersUI {
            return ListCharactersUI(

                results = data.results.map {
                    Result(
                        it.id,
                        it.image,
                        it.name,
                    )
                }
            )
        }

        fun infoFromDomain(info: ListCharactersDomain.Info?): PageInfo {

            info ?: return PageInfo(1, 1, 1, true)
            return PageInfo(
                info.count,
                info.pages,
                if (info.next == null) info.pages else info.next.substringAfterLast("=").toInt() - 1,
                info.next == null
            )
        }
    }

}