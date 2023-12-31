package com.aluengo.cleancomposerickandmorty.listCharacters.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pullrefresh.PullRefreshIndicator
import androidx.compose.material3.pullrefresh.pullRefresh
import androidx.compose.material3.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.aluengo.cleancomposerickandmorty.R
import com.aluengo.cleancomposerickandmorty.core.ui.CustomIcon
import com.aluengo.cleancomposerickandmorty.core.ui.ScaffoldView
import com.aluengo.cleancomposerickandmorty.core.ui.ViewConfig
import com.aluengo.cleancomposerickandmorty.core.utils.TestTags

@Composable
@Preview
fun ListCharactersScreenPreview() {
    ListCharactersScreen(
        ListCharactersState(
            data = listOf(
                ListCharactersUI.Result(
                    1,
                    "",
                    "Rick Sanches",
                    "Borg",
                    "Namek",
                    "12",
                    "Specter"
                ), ListCharactersUI.Result(
                    2,
                    "",
                    "Rick Sanches",
                    "Borg",
                    "Namek",
                    "12",
                    "Specter"
                ), ListCharactersUI.Result(
                    23,
                    "",
                    "Rick Sanches",
                    "Borg",
                    "Namek",
                    "12",
                    "Specter"
                ), ListCharactersUI.Result(
                    24,
                    "",
                    "Rick Sanches Sanches Sanches",
                    "Borg",
                    "Namek",
                    "12",
                    "Specter"
                )
            )
        ),
        SnackbarHostState()
    ) {}
}

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
        ScaffoldView(
            ViewConfig(
                title = LocalContext.current.getString(R.string.app_name),
                onSearchClicked = {
                    submitIntent(ListCharactersIntent.OnSearchClicked)
                }), searchWidgetState = state.searchState,
            searchTextState = state.searchText,
            onTextChange = {
                submitIntent(ListCharactersIntent.OnTypeSearch(it))
            }, onCloseClicked = {
                submitIntent(ListCharactersIntent.OnCloseSearchClick)
            }, onSearch = {
                submitIntent(ListCharactersIntent.OnSearch(it))
            }, AppBarContent = {
                CustomIcon(Icons.Filled.Search, Color.White, modifier = Modifier.testTag(TestTags.SearchIcon)) {
                    submitIntent(ListCharactersIntent.OnSearchClicked)
                }

            }, snackbarHostState = snackbarHostState
        ) { paddingValues ->

            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {

                ListItem(state, submitIntent)

                if (state.data.isEmpty() && !state.isLoading) {
                    Text(
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(Alignment.Center),
                        text = LocalContext.current.getString(R.string.list_character_empty)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListItem(
    state: ListCharactersState,
    submitIntent: (ListCharactersIntent) -> Unit
) {

    val freshState = rememberPullRefreshState(state.isLoading, {
        submitIntent(ListCharactersIntent.Load)
    })

    Box(
        Modifier
            .fillMaxWidth()
            .pullRefresh(freshState)
    ) {
        LazyVerticalGrid(columns = GridCells.Fixed(2), content = {
            items(items = state.data, key = { it.id }) {
                CharacterView(Modifier.animateItemPlacement(), it, submitIntent)
            }
            item {
                LaunchedEffect(true) {
                    submitIntent(ListCharactersIntent.EndOfListReached)
                }
            }
        }, contentPadding = PaddingValues(0.dp))


        PullRefreshIndicator(state.isLoading, freshState, Modifier.align(Alignment.TopCenter))
    }
}


@Composable
fun CharacterView(
    modifier: Modifier,
    item: ListCharactersUI.Result,
    submitIntent: (ListCharactersIntent) -> Unit
) {
    Column(
        modifier = modifier
            .padding(8.dp).testTag(TestTags.CharacterView)
            .clickable {
                submitIntent(ListCharactersIntent.OnCharacterSelected(item))
            }
    ) {
        Box(

            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .height(230.dp)
        ) {


            Card(
                border = BorderStroke(1.dp, Color.LightGray),
                shape = RoundedCornerShape(16.dp), modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(0.dp)

            ) {
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .height(50.dp)
                ) {
                    Text(
                        modifier = Modifier

                            .align(Alignment.Center)
                            .fillMaxWidth(),
                        fontSize = 16.sp,
                        style = MaterialTheme.typography.titleLarge,
                        text = item.name,
                        textAlign = TextAlign.Center
                    )
                }
            }

            AsyncImage(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(0.dp)
                    .size(160.dp)
                    .clip(RoundedCornerShape(100)),
                contentScale = ContentScale.Crop,
                error = painterResource(R.drawable.character_placeholder),
                placeholder = painterResource(R.drawable.character_placeholder),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(item.image)
                    .crossfade(true)
                    .diskCacheKey(item.image)
                    .memoryCacheKey(item.image)
                    .build(),
                contentDescription = "Character Image",

                )
        }
    }

}