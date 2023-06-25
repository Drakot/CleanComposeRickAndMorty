package com.aluengo.cleancomposerickandmorty.listCharacters.presentation

import android.content.Context
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.aluengo.cleancomposerickandmorty.core.data.MockData
import com.aluengo.cleancomposerickandmorty.core.di.LocalStorageModule
import com.aluengo.cleancomposerickandmorty.core.di.NetworkModule
import com.aluengo.cleancomposerickandmorty.core.navigation.NavRoutes
import com.aluengo.cleancomposerickandmorty.core.navigation.assertCurrentRouteName
import com.aluengo.cleancomposerickandmorty.core.utils.TestTags
import com.aluengo.cleancomposerickandmorty.core.utils.TestTags.LeadingSearchIcon
import com.aluengo.cleancomposerickandmorty.listCharacters.data.ListCharactersResponse
import com.aluengo.cleancomposerickandmorty.main.App
import com.aluengo.cleancomposerickandmorty.main.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@UninstallModules(LocalStorageModule::class, NetworkModule::class)
@RunWith(AndroidJUnit4::class)
class ListCharactersScreenTest {
    private lateinit var context: Context
    private lateinit var navController: NavHostController
    private var expectedCharacter: ListCharactersResponse.Result? = null

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var mockData: MockData

    @ExperimentalAnimationApi
    @Before
    fun setUp() {
        hiltRule.inject()
        expectedCharacter = mockData.createListCharactersResponse().results.firstOrNull()

        composeRule.activity.setContent {
            context = LocalContext.current
            navController = rememberNavController()
            App(navController = navController)
        }
    }

    @Test
    fun clickSearch_isVisible() {
        composeRule.onNodeWithTag(LeadingSearchIcon).assertDoesNotExist()
        composeRule.onNodeWithTag(TestTags.SearchIcon).performClick()
        composeRule.onNodeWithTag(LeadingSearchIcon).assertIsDisplayed()
    }

    @Test
    fun listCharactersScreen_loadData() {
        composeRule.onNodeWithText(expectedCharacter?.name ?: "").assertIsDisplayed()
    }

    @Test
    fun listCharactersScreen_clickCharacter() {
        composeRule.onAllNodesWithTag(TestTags.CharacterView)[0].performClick()
        navController.assertCurrentRouteName(NavRoutes.Character.route)
        composeRule.onNodeWithText(expectedCharacter?.status ?: "").assertIsDisplayed()
    }

    @Test
    fun listCharactersScreen_performSearch() {
        val notExpectedCharacter = mockData.createListCharactersResponse().results[0]
        val expectedCharacter = mockData.createListCharactersResponse().results[1]
        composeRule.onNodeWithTag(TestTags.SearchIcon).performClick()

        composeRule.onNodeWithTag(TestTags.SearchField).performTextInput("morty")
        composeRule.onNodeWithTag(TestTags.SearchField).assertTextContains("morty")

        composeRule.onNodeWithTag(TestTags.SearchField).performImeAction()

        composeRule.onNodeWithText(expectedCharacter.name).assertIsDisplayed()
        composeRule.onNodeWithText(notExpectedCharacter.name).assertDoesNotExist()
    }
}

