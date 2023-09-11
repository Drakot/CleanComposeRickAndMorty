package com.aluengo.cleancomposerickandmorty.listCharacters.data

import com.aluengo.cleancomposerickandmorty.core.data.Mapper
import com.aluengo.cleancomposerickandmorty.listCharacters.data.local.CharacterLocalModel
import com.aluengo.cleancomposerickandmorty.listCharacters.data.local.InfoLocalModel
import com.aluengo.cleancomposerickandmorty.listCharacters.domain.ListCharactersDomain

class MockData(val mapper: Mapper) {

    fun createListCharactersDomain(number: Int = 2): ListCharactersDomain {
        val localTestData = createListCharactersLocal(number)
        val infoData = createListCharactersInfoLocal()
        val characters = mapper.toDomainCharacters(localTestData)!!
        val info = mapper.toDomainCharactersInfo(infoData)!!

        return ListCharactersDomain(info, characters)
    }

    fun createListCharactersLocal(number: Int = 2): List<CharacterLocalModel> {
        val testData = createListCharactersResponse(number)
        return mapper.toLocalCharacters(testData)!!
    }

    fun createListCharactersInfoLocal(): InfoLocalModel {
        val testData = createListCharactersResponse()
        return mapper.toLocalCharactersPagination(testData)!!
    }

    fun createListCharactersResponse(number: Int = 2): ListCharactersResponse {
        val info = ListCharactersResponse.Info(
            count = 2,
            next = "https://api.example.com/characters?page=2",
            pages = 3,
            prev = "https://api.example.com/characters?page=1"
        )

        val result1 = ListCharactersResponse.Result(
            created = "2023-06-01T12:00:00Z",
            episode = listOf(
                "https://api.example.com/episodes/1",
                "https://api.example.com/episodes/2"
            ),
            gender = "Male",
            id = 1,
            image = "https://api.example.com/images/1.png",
            location = ListCharactersResponse.Result.Location(
                "Earth",
                "https://api.example.com/locations/1"
            ),
            name = "Rick Sanchez",
            origin = ListCharactersResponse.Result.Origin(
                "Earth",
                "https://api.example.com/origins/1"
            ),
            species = "Human",
            status = "Alive",
            type = "Main",
            url = "https://api.example.com/characters/1",
        )

        var results = emptyList<ListCharactersResponse.Result>()
        if (number > 2) {
            results = (1..20).map {
                characterResult(it)
            }
        } else {
            results = listOf(result1, characterResult())
        }
        return ListCharactersResponse(info, results)
    }

    fun characterResult(id: Int = 2): ListCharactersResponse.Result {
        return ListCharactersResponse.Result(
            created = "2023-06-02T12:00:00Z",
            episode = listOf(
                "https://api.example.com/episodes/3",
                "https://api.example.com/episodes/4"
            ),
            gender = "Female",
            id = id,
            image = "https://api.example.com/images/2.png",
            location = ListCharactersResponse.Result.Location(
                "Space",
                "https://api.example.com/locations/2"
            ),
            name = "Morty Smith",
            origin = ListCharactersResponse.Result.Origin(
                "Earth",
                "https://api.example.com/origins/1"
            ),
            species = "Human",
            status = "Alive",
            type = "Main",
            url = "https://api.example.com/characters/2"
        )
    }
}