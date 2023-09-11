package com.aluengo.cleancomposerickandmorty

import app.cash.turbine.ReceiveTurbine
import app.cash.turbine.test
import io.mockk.clearAllMocks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@ExperimentalCoroutinesApi
abstract class BaseTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var testDispatcher = MainCoroutineRule()

    @Before
    fun baseSetup() {
    }

    @After
    fun baseTearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }
}

@ExperimentalCoroutinesApi
class MainCoroutineRule(private val dispatcher: TestDispatcher = StandardTestDispatcher()) :
    TestWatcher() {

    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: Description?) {
        super.finished(description)
        Dispatchers.resetMain()
    }
}


@OptIn(ExperimentalCoroutinesApi::class)
public suspend fun <T> Flow<T>.testFlow(
    scope: TestScope,
    onFinish: suspend ReceiveTurbine<T>.() -> Unit,
) {
    test {

        scope.advanceUntilIdle()
        onFinish()

        cancelAndConsumeRemainingEvents()
    }
}