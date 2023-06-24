package com.aluengo.cleancomposerickandmorty.listCharacters.data

import com.aluengo.cleancomposerickandmorty.core.data.Mapper
import com.aluengo.cleancomposerickandmorty.listCharacters.data.local.CharacterLocalModel
import com.aluengo.cleancomposerickandmorty.listCharacters.data.local.InfoLocalModel
import com.aluengo.cleancomposerickandmorty.listCharacters.domain.ListCharactersDomain

class MockData (val mapper: Mapper) {

    fun createListCharactersDomain(): ListCharactersDomain {
        val localTestData = createListCharactersLocal()
        val infoData = createListCharactersInfoLocal()
        val characters = mapper.toDomainCharacters(localTestData)!!
        val info = mapper.toDomainCharactersInfo(infoData)!!

        return ListCharactersDomain(info, characters)
    }

    fun createListCharactersLocal(): List<CharacterLocalModel> {
        val testData = createListCharactersResponse()
        return mapper.toLocalCharacters(testData)!!
    }

    fun createListCharactersInfoLocal(): InfoLocalModel {
        val testData = createListCharactersResponse()
        return mapper.toLocalCharactersPagination(testData)!!
    }

    fun createListCharactersResponse(): ListCharactersResponse {
        val info = ListCharactersResponse.Info(
            count = 2,
            next = "https://api.example.com/characters?page=2",
            pages = 3,
            prev = "https://api.example.com/characters?page=1"
        )

        val result1 = ListCharactersResponse.Result(
            created = "2023-06-01T12:00:00Z",
            episode = listOf("https://api.example.com/episodes/1", "https://api.example.com/episodes/2"),
            gender = "Male",
            id = 1,
            image = "https://api.example.com/images/1.png",
            location = ListCharactersResponse.Result.Location("Earth", "https://api.example.com/locations/1"),
            name = "Rick Sanchez",
            origin = ListCharactersResponse.Result.Origin("Earth", "https://api.example.com/origins/1"),
            species = "Human",
            status = "Alive",
            type = "Main",
            url = "https://api.example.com/characters/1"
        )

        val result2 = ListCharactersResponse.Result(
            created = "2023-06-02T12:00:00Z",
            episode = listOf("https://api.example.com/episodes/3", "https://api.example.com/episodes/4"),
            gender = "Female",
            id = 2,
            image = "https://api.example.com/images/2.png",
            location = ListCharactersResponse.Result.Location("Space", "https://api.example.com/locations/2"),
            name = "Morty Smith",
            origin = ListCharactersResponse.Result.Origin("Earth", "https://api.example.com/origins/1"),
            species = "Human",
            status = "Alive",
            type = "Main",
            url = "https://api.example.com/characters/2"
        )

        val results = listOf(result1, result2)

        return ListCharactersResponse(info, results)
    }
}