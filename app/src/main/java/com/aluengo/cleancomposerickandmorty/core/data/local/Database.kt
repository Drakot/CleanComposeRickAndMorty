package com.aluengo.cleancomposerickandmorty.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aluengo.cleancomposerickandmorty.listCharacters.data.local.CharacterDao
import com.aluengo.cleancomposerickandmorty.listCharacters.data.local.CharacterLocalModel
import com.aluengo.cleancomposerickandmorty.listCharacters.data.local.InfoDao
import com.aluengo.cleancomposerickandmorty.listCharacters.data.local.InfoLocalModel

@Database(
    entities = [CharacterLocalModel::class, InfoLocalModel::class],
    version = 1
)
abstract class Database : RoomDatabase() {

    abstract val characterDao: CharacterDao
    abstract val infoDao: InfoDao

    companion object {
        const val DATABASE_NAME = "characters_db"
    }
}