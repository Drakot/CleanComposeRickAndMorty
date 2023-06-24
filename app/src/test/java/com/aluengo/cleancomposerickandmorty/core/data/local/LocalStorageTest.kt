package com.aluengo.cleancomposerickandmorty.core.data.local

import com.aluengo.cleancomposerickandmorty.BaseTest
import com.aluengo.cleancomposerickandmorty.core.data.Mapper
import com.aluengo.cleancomposerickandmorty.listCharacters.data.MockData
import com.aluengo.cleancomposerickandmorty.listCharacters.data.local.CharacterDao
import com.aluengo.cleancomposerickandmorty.listCharacters.data.local.CharacterLocalModel
import com.aluengo.cleancomposerickandmorty.listCharacters.data.local.InfoDao
import com.aluengo.cleancomposerickandmorty.listCharacters.data.local.InfoLocalModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class LocalStorageTest : BaseTest() {

    private lateinit var mockData: MockData
    private lateinit var characterDao: CharacterDao
    private lateinit var infoDao: InfoDao
    private lateinit var localStorage: LocalStorage

    @Before
    fun setUp() {
        characterDao = mockk(relaxed = true)
        infoDao = mockk(relaxed = true)
        localStorage = LocalStorage(characterDao, infoDao)
        mockData = MockData(Mapper())
    }

    @Test
    fun testRemoveData() = runTest {
        coEvery { characterDao.clearAll() } just runs
        coEvery { infoDao.clearAll() } just runs

        localStorage.removeData()

        coVerify(exactly = 1) { characterDao.clearAll() }
        coVerify(exactly = 1) { infoDao.clearAll() }
    }

    @Test
    fun testSaveCharacters_withNonNullData() = runTest {
        val data = mockData.createListCharactersLocal()

        coEvery { characterDao.upsertAll(data) } just runs

        localStorage.saveCharacters(data)

        coVerify(exactly = 1) { characterDao.upsertAll(data) }
    }

    @Test
    fun testSaveCharacters_withNullData() = runTest {
        val data: List<CharacterLocalModel>? = null

        localStorage.saveCharacters(data)

        coVerify(exactly = 0) { characterDao.upsertAll(any()) }
    }

    @Test
    fun testGetCharacters_withFilter() = runTest {
        val filter = "someFilter"

        val expectedData = mockData.createListCharactersLocal()

        every { characterDao.getCharactersByName(filter) } returns flowOf(expectedData)

        val data = localStorage.getCharacters(filter).first()
        val actualData = mutableListOf<CharacterLocalModel>()
        actualData.addAll(data)

        assertEquals(expectedData, actualData)
        verify(exactly = 1) { characterDao.getCharactersByName(filter) }
        verify(exactly = 0) { characterDao.getCharacters() }
    }

    @Test
    fun testGetCharacters_withNullFilter() = runTest {
        val expectedData = mockData.createListCharactersLocal()

        every { characterDao.getCharacters() } returns flowOf(expectedData)

        val actualData = localStorage.getCharacters(null).first()

        assertEquals(expectedData, actualData)
        verify(exactly = 1) { characterDao.getCharacters() }
        verify(exactly = 0) { characterDao.getCharactersByName(any()) }
    }

    @Test
    fun testGetInfo() = runTest {
        val expectedInfo = mockData.createListCharactersInfoLocal()

        every { infoDao.getInfo() } returns flowOf(expectedInfo)

        val flow = localStorage.getInfo()
        val actualInfo = flow.single()

        assertEquals(expectedInfo, actualInfo)
        verify(exactly = 1) { infoDao.getInfo() }
    }

    @Test
    fun testSaveCharactersPagination_withNonNullData() = runTest {
        val data = mockData.createListCharactersInfoLocal()

        coEvery { infoDao.upsertAll(data) } just runs

        localStorage.saveCharactersPagination(data)

        coVerify(exactly = 1) { infoDao.upsertAll(data) }
    }

    @Test
    fun testSaveCharactersPagination_withNullData() = runTest {
        val data: InfoLocalModel? = null

        localStorage.saveCharactersPagination(data)

        coVerify(exactly = 0) { infoDao.upsertAll(any()) }
    }
}