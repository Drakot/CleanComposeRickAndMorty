package com.aluengo.cleancomposerickandmorty.core.data

import com.aluengo.cleancomposerickandmorty.BaseTest
import com.aluengo.cleancomposerickandmorty.listCharacters.data.MockData
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MapperTest() : BaseTest() {

    private lateinit var mockData: MockData
    private lateinit var sut: Mapper


    @Before
    fun setUp() {
        sut = Mapper()
        mockData = MockData(sut)
    }


    @Test
    fun testToDomainCharacters() {

        val input = mockData.createListCharactersLocal()

        val expectedOutput = mockData.createListCharactersDomain().results

        val actualOutput = sut.toDomainCharacters(input)

        assertEquals(expectedOutput, actualOutput)
    }

    @Test
    fun testToDomainCharacter() {

        val input = mockData.createListCharactersResponse().results.first()

        val expectedOutput = mockData.createListCharactersDomain().results.first()

        val actualOutput = sut.toDomainCharacter(input)

        assertEquals(expectedOutput, actualOutput)
    }

    @Test
    fun testToDomainCharactersInfo() {
        val input = mockData.createListCharactersInfoLocal()

        val expectedOutput = mockData.createListCharactersDomain().info

        val actualOutput = sut.toDomainCharactersInfo(input)

        assertEquals(expectedOutput, actualOutput)
    }

    @Test
    fun testToLocalCharacters() {
        val input = mockData.createListCharactersResponse()

        val expectedOutput = mockData.createListCharactersLocal()

        val actualOutput = sut.toLocalCharacters(input)

        assertEquals(expectedOutput, actualOutput)
    }

    @Test
    fun testToLocalCharactersPagination() {
        val input = mockData.createListCharactersResponse()

        val expectedOutput = mockData.createListCharactersInfoLocal()

        val actualOutput = sut.toLocalCharactersPagination(input)

        assertEquals(expectedOutput, actualOutput)
    }

    @Test
    fun `when list characters response is null must return null`() {
        val actualOutput = sut.toLocalCharacters(null)

        assertEquals(null, actualOutput)
    }

    @Test
    fun `when character response to local is null must return null`() {
        val actualOutput = sut.toLocalCharacter(null)

        assertEquals(null, actualOutput)
    }

    @Test
    fun `when character local is null must return null`() {
        val actualOutput = sut.toDomainCharacterFromLocal(null)

        assertEquals(null, actualOutput)
    }

    @Test
    fun `when InfoLocalModel is null must return null`() {
        val actualOutput = sut.toDomainCharactersInfo(null)

        assertEquals(null, actualOutput)
    }

    @Test
    fun `when list characters pagination response is null must return null`() {
        val actualOutput = sut.toLocalCharactersPagination(null)

        assertEquals(null, actualOutput)
    }

    @Test
    fun `when character response is null must return null`() {
        val actualOutput = sut.toDomainCharacter(null)

        assertEquals(null, actualOutput)
    }


}