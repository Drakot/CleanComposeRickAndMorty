package com.aluengo.cleancomposerickandmorty.detailCharacter.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.aluengo.cleancomposerickandmorty.R
import com.aluengo.cleancomposerickandmorty.core.navigation.CharacterItemInput
import com.aluengo.cleancomposerickandmorty.core.ui.ScaffoldView
import com.aluengo.cleancomposerickandmorty.core.ui.VerticalSpacer
import com.aluengo.cleancomposerickandmorty.core.ui.ViewConfig
import com.aluengo.cleancomposerickandmorty.listCharacters.presentation.ListCharactersUI


@Composable
@Preview
fun CharacterScreenPreview() {
    CharacterScreen(
        CharacterState(
            data = ListCharactersUI.Result(
                1,
                "",
                "Rick Sanches",
                "Borg",
                "Namek",
                "12",
                "Specter"
            )
        ),
        CharacterItemInput(),
        SnackbarHostState()
    ) {}
}

@Composable
fun CharacterScreen(
    viewState: CharacterState,
    characterItemInput: CharacterItemInput,
    snackbarHostState: SnackbarHostState,
    submitIntent: (CharacterIntent) -> Unit
) {

    LaunchedEffect(true) {
        submitIntent(CharacterIntent.Load(characterItemInput))
    }

    val context = LocalContext.current
    val item = viewState.data

    Box(modifier = Modifier.fillMaxSize()) {
        ScaffoldView(
            ViewConfig(
                title = item?.name,
                showBackButton = true,
                onClickNavIcon = {
                    submitIntent(CharacterIntent.OnClickNavIcon)
                }
            ), snackbarHostState = snackbarHostState
        ) { paddingValues ->

            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(0.85f)
                        .height(300.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop,
                    error = painterResource(R.drawable.character_placeholder),
                    placeholder = painterResource(R.drawable.character_placeholder),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(item?.image)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Character Image",
                )
                VerticalSpacer(8)
                Text(
                    style = MaterialTheme.typography.titleLarge,
                    text = item?.name ?: "",
                    textAlign = TextAlign.Center
                )
                VerticalSpacer(8)
                Info(context.getString(R.string.character_detail_species), item?.species)
                VerticalSpacer()
                Info(context.getString(R.string.character_detail_origin), item?.origin)
                VerticalSpacer()
                Info(context.getString(R.string.character_detail_episodes), item?.episodes)
                VerticalSpacer()
                Info(context.getString(R.string.character_detail_status), item?.status)
            }
        }
    }

}