package com.aluengo.cleancomposerickandmorty.core.data

import com.aluengo.cleancomposerickandmorty.listCharacters.data.ListCharactersResponse
import com.aluengo.cleancomposerickandmorty.listCharacters.data.local.CharacterLocalModel
import com.aluengo.cleancomposerickandmorty.listCharacters.data.local.InfoLocalModel
import com.aluengo.cleancomposerickandmorty.listCharacters.domain.ListCharactersDomain

class Mapper {


    fun toDomainCharacters(input: List<CharacterLocalModel>?): List<ListCharactersDomain.Result>? {
        return input?.map {
            toDomainCharacterFromLocal(it)!!
        }
    }

    fun toDomainCharacterFromLocal(input: CharacterLocalModel?): ListCharactersDomain.Result? {
        input?.apply {
            return ListCharactersDomain.Result(
                id,
                gender,
                image,
                name,
                species,
                status,
                type,
                url,
                origin.name,
                episode.size.toString()
            )
        }
        return null
    }


    fun toDomainCharacter(input: ListCharactersResponse.Result?): ListCharactersDomain.Result? {
        input?.apply {
            return ListCharactersDomain.Result(
                id,
                gender,
                image,
                name,
                species,
                status,
                type,
                url,
                origin.name,
                episode.size.toString()
            )
        }

        return null
    }


    fun toDomainCharactersInfo(input: InfoLocalModel?): ListCharactersDomain.Info? {
        input?.let {
            return ListCharactersDomain.Info(input.count, input.next, input.pages, input.prev)
        }
        return null
    }

    fun toLocalCharacters(input: ListCharactersResponse?): List<CharacterLocalModel>? {
        input?.apply {
            return results.map {
                toLocalCharacter(it)!!
            }
        }
        return null
    }

    fun toLocalCharacter(input: ListCharactersResponse.Result?): CharacterLocalModel? {
        input?.apply {
            return CharacterLocalModel(
                id,
                name,
                gender,
                image,
                created,
                species,
                status,
                type,
                url,
                episode,
                CharacterLocalModel.Location(location.name, location.url),
                CharacterLocalModel.Origin(origin.name, origin.url)
            )
        }

        return null
    }

    fun toLocalCharactersPagination(input: ListCharactersResponse?): InfoLocalModel? {
        input?.apply {
            return InfoLocalModel(info.count, info.next, info.pages, info.prev)
        }
        return null
    }

    fun toDomainCharactersResponse(input: ListCharactersResponse?): ListCharactersDomain? {
        input?.apply {
            return toDomainCharactersInfo(info)?.let {
                toDomainCharactersFromList(results)?.let { it1 ->
                    ListCharactersDomain(
                        it,
                        it1

                    )
                }
            }
        }
        return null
    }

    private fun toDomainCharactersFromList(input: List<ListCharactersResponse.Result>?): List<ListCharactersDomain.Result>? {
        input?.let {
            return input.map {
                toDomainCharacterResult(it)
            }
        }
        return null
    }

    private fun toDomainCharacterResult(input: ListCharactersResponse.Result): ListCharactersDomain.Result {
        input.apply {
            return ListCharactersDomain.Result(
                id,
                gender,
                image,
                name,
                species,
                status,
                type,
                url,
                origin.name,
                episode.size.toString()
            )
        }
    }

    private fun toDomainCharactersInfo(input: ListCharactersResponse.Info?): ListCharactersDomain.Info? {
        input?.let {
            return ListCharactersDomain.Info(input.count, input.next, input.pages, input.prev)
        }
        return null
    }

}