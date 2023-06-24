package com.aluengo.cleancomposerickandmorty.listCharacters.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface InfoDao {
    @Query("SELECT * FROM infolocalmodel WHERE id = 1")
    fun getInfo(): Flow<InfoLocalModel?>

    @Query("DELETE FROM infolocalmodel")
    suspend fun clearAll()

    @Upsert
    suspend fun upsertAll(info: InfoLocalModel)
}