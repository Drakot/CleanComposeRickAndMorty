package com.aluengo.cleancomposerickandmorty.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aluengo.cleancomposerickandmorty.core.navigation.NavRoutes
import com.aluengo.cleancomposerickandmorty.core.ui.ErrorMapper
import com.aluengo.cleancomposerickandmorty.core.ui.mvi.collectInLaunchedEffectWithLifecycle
import com.aluengo.cleancomposerickandmorty.core.ui.theme.CleanComposeRickAndMortyTheme
import com.aluengo.cleancomposerickandmorty.core.utils.logd
import com.aluengo.cleancomposerickandmorty.detailCharacter.presentation.CharacterScreen
import com.aluengo.cleancomposerickandmorty.detailCharacter.presentation.CharacterUiSingleEvent
import com.aluengo.cleancomposerickandmorty.detailCharacter.presentation.CharacterViewModel
import com.aluengo.cleancomposerickandmorty.listCharacters.presentation.ListCharactersScreen
import com.aluengo.cleancomposerickandmorty.listCharacters.presentation.ListCharactersUiSingleEvent
import com.aluengo.cleancomposerickandmorty.listCharacters.presentation.ListCharactersViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CleanComposeRickAndMortyTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                   App()
                }
            }
        }
    }
}


@Composable
fun App(navController: NavHostController = rememberNavController()) {
    //val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val errorMapper = ErrorMapper(LocalContext.current)
    NavHost(
        navController, startDestination = NavRoutes.ListCharacters.route
    ) {
        composable(route = NavRoutes.ListCharacters.route) {
            val viewModel = hiltViewModel<ListCharactersViewModel>()

            ListCharactersScreen(viewModel.viewState.collectAsState().value, snackbarHostState, viewModel::submitIntent)

            viewModel.singleEvent.collectInLaunchedEffectWithLifecycle { event ->
                when (event) {
                    is ListCharactersUiSingleEvent.ShowMessage -> {
                        println(event.message)

                        scope.showSnackBar(snackbarHostState, event.message)
                    }

                    is ListCharactersUiSingleEvent.ShowError -> {
                        logd(event.errorType.toString())
                        scope.showSnackBar(snackbarHostState, errorMapper.map(event.errorType))
                    }

                    is ListCharactersUiSingleEvent.OpenCharacter -> {
                        navController.navigate(event.navRoute)
                    }
                }
            }
        }

        composable(route = NavRoutes.Character.route) {
            val viewModel = hiltViewModel<CharacterViewModel>()

            CharacterScreen(
                viewModel.viewState.collectAsState().value,
                NavRoutes.Character.fromEntry(it),
                snackbarHostState,
                viewModel::submitIntent
            )

            viewModel.singleEvent.collectInLaunchedEffectWithLifecycle { event ->
                when (event) {
                    is CharacterUiSingleEvent.GoBack -> {
                        navController.popBackStack()
                    }

                    is CharacterUiSingleEvent.ShowError -> {
                        scope.showSnackBar(snackbarHostState, errorMapper.map(event.errorType))
                    }
                }
            }
        }
    }
}

fun CoroutineScope.showSnackBar(snackbarHostState: SnackbarHostState, message: String) {
    this.launch {
        snackbarHostState.showSnackbar(message)
    }
}