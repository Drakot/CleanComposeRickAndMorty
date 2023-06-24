package com.aluengo.cleancomposerickandmorty.listCharacters.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {
    @Query("SELECT * FROM characterlocalmodel WHERE name LIKE '%' || :filter || '%'")
    fun getCharactersByName(filter: String): Flow<List<CharacterLocalModel>>

    @Query("SELECT * FROM characterlocalmodel")
    fun getCharacters(): Flow<List<CharacterLocalModel>>


    @Query("SELECT * FROM characterlocalmodel WHERE id = :id")
    fun getCharacter(id: Int): Flow<CharacterLocalModel?>

    @Upsert
    suspend fun upsertAll(characters: List<CharacterLocalModel>)

    @Query("DELETE FROM characterlocalmodel")
    suspend fun clearAll()

    @Upsert
    suspend fun upsert(character: CharacterLocalModel)

}