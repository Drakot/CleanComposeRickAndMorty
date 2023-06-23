package com.aluengo.cleancomposerickandmorty.listCharacters.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier

@Composable
fun ListCharactersScreen(
    state: ListCharactersState,
    snackbarHostState: SnackbarHostState,
    submitIntent: (ListCharactersIntent) -> Unit
) {

    LaunchedEffect(true) {
        submitIntent(ListCharactersIntent.Load)
    }

    Box(modifier = Modifier.fillMaxSize()) {

    }
}

