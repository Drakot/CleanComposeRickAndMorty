package com.aluengo.cleancomposerickandmorty.detailCharacter.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.aluengo.cleancomposerickandmorty.core.navigation.CharacterItemInput

@Composable
fun CharacterScreen(
    viewState: CharacterState,
    characterItemInput: CharacterItemInput,
    snackbarHostState: SnackbarHostState,
    submitIntent: (CharacterIntent) -> Unit
) {

    LaunchedEffect(true) {

        submitIntent(CharacterIntent.Load(characterItemInput.id))
    }

}