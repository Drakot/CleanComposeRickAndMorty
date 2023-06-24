package com.aluengo.cleancomposerickandmorty.core.data.local

import com.aluengo.cleancomposerickandmorty.listCharacters.data.local.CharacterDao
import com.aluengo.cleancomposerickandmorty.listCharacters.data.local.CharacterLocalModel
import com.aluengo.cleancomposerickandmorty.listCharacters.data.local.InfoDao
import com.aluengo.cleancomposerickandmorty.listCharacters.data.local.InfoLocalModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalStorage @Inject constructor(
    private val characterDao: CharacterDao,
    private val infoDao: InfoDao
) {

    suspend fun removeData() {
        characterDao.clearAll()
        infoDao.clearAll()
    }

    suspend fun saveCharacters(data: List<CharacterLocalModel>?) {
        data?.let { characterDao.upsertAll(it) }
    }


    fun getCharacters(filter: String?): Flow<List<CharacterLocalModel>> {
        if (!filter.isNullOrEmpty()) {
            return characterDao.getCharactersByName(filter)
        }

        return characterDao.getCharacters()
    }


    suspend fun getInfo(): Flow<InfoLocalModel?> = infoDao.getInfo()

    suspend fun saveCharactersPagination(data: InfoLocalModel?) {
        data?.let { infoDao.upsertAll(it) }
    }

    suspend fun saveCharacter(data: CharacterLocalModel?) {
        data?.let { characterDao.upsert(it) }
    }

    fun getCharacter(id: Int): Flow<CharacterLocalModel?> {
        return characterDao.getCharacter(id)
    }

}