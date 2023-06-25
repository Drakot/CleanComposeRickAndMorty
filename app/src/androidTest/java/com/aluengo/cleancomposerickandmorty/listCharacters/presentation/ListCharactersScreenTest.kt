package com.aluengo.cleancomposerickandmorty.listCharacters.presentation

import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.aluengo.cleancomposerickandmorty.core.data.MockData
import com.aluengo.cleancomposerickandmorty.core.di.LocalStorageModule
import com.aluengo.cleancomposerickandmorty.core.di.NetworkModule
import com.aluengo.cleancomposerickandmorty.core.navigation.NavRoutes
import com.aluengo.cleancomposerickandmorty.core.ui.theme.CleanComposeRickAndMortyTheme
import com.aluengo.cleancomposerickandmorty.core.utils.TestTags
import com.aluengo.cleancomposerickandmorty.core.utils.TestTags.LeadingSearchIcon
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
        composeRule.activity.setContent {
            val snackbarHostState = remember { SnackbarHostState() }
            val navController = rememberNavController()
            CleanComposeRickAndMortyTheme {
                NavHost(
                    navController = navController,
                    startDestination = NavRoutes.ListCharacters.route
                ) {
                    composable(route = NavRoutes.ListCharacters.route) {
                        val viewModel = hiltViewModel<ListCharactersViewModel>()
                        ListCharactersScreen(
                            viewModel.viewState.collectAsState().value,
                            snackbarHostState,
                            viewModel::submitIntent
                        )

                    }
                }
            }
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
        val expecetedName = mockData.createListCharactersResponse().results.firstOrNull()?.name ?:""
        composeRule.waitForIdle()
        composeRule.onNodeWithText(expecetedName).assertIsDisplayed()
    }
}

