package com.aluengo.cleancomposerickandmorty.detailCharacter.presentation

import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.aluengo.cleancomposerickandmorty.core.data.MockData
import com.aluengo.cleancomposerickandmorty.core.di.LocalStorageModule
import com.aluengo.cleancomposerickandmorty.core.di.NetworkModule
import com.aluengo.cleancomposerickandmorty.core.navigation.CharacterItemInput
import com.aluengo.cleancomposerickandmorty.core.navigation.NavRoutes
import com.aluengo.cleancomposerickandmorty.core.ui.theme.CleanComposeRickAndMortyTheme
import com.aluengo.cleancomposerickandmorty.listCharacters.data.ListCharactersResponse
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
class CharacterScreenTest {
    private lateinit var expectedCharacter: ListCharactersResponse.Result

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
        expectedCharacter = mockData.createListCharactersResponse().results.first()

        composeRule.activity.setContent {
            val navController = rememberNavController()
            CleanComposeRickAndMortyTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val snackbarHostState = remember { SnackbarHostState() }
                    NavHost(
                        navController, startDestination = NavRoutes.Character.route
                    ) {
                        composable(route = NavRoutes.Character.route) {
                            val viewModel = hiltViewModel<CharacterViewModel>()

                            CharacterScreen(
                                viewModel.viewState.collectAsState().value,
                                CharacterItemInput(expectedCharacter.id, expectedCharacter.name),
                                snackbarHostState,
                                viewModel::submitIntent
                            )
                        }
                    }
                }
            }
        }
    }

    @Test
    fun characterScreen_loadData() {
        composeRule.onAllNodesWithText(expectedCharacter.name).assertCountEquals(2)
        composeRule.onNodeWithText(expectedCharacter.status).assertIsDisplayed()
        composeRule.onNodeWithText(expectedCharacter.species).assertIsDisplayed()
    }
}