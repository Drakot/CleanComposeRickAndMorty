package com.aluengo.cleancomposerickandmorty.core.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aluengo.cleancomposerickandmorty.R
import com.aluengo.cleancomposerickandmorty.core.utils.TestTags
import com.aluengo.cleancomposerickandmorty.core.utils.TestTags.LeadingSearchIcon

@Composable
fun MyTopAppBar(
    viewConfig: ViewConfig = ViewConfig(),
    searchWidgetState: SearchWidgetState,
    searchTextState: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearch: (String) -> Unit,
    Content: @Composable () -> Unit = {}
) {
    when (searchWidgetState) {
        SearchWidgetState.CLOSED -> {
            DefaultAppBar(viewConfig) { Content() }
        }

        SearchWidgetState.OPENED -> {

            SearchAppBar(
                text = searchTextState,
                onTextChange = onTextChange,
                onCloseClicked = onCloseClicked,
                onSearch = onSearch
            )

        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultAppBar(
    viewConfig: ViewConfig = ViewConfig(),
    Content: @Composable () -> Unit = {}
) {
    TopAppBar(
        title = {
            Text(text = viewConfig.title ?: "")
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = viewConfig.appbarBackgroundColor ?: MaterialTheme.colorScheme.secondary,
            actionIconContentColor = Color.White,
            titleContentColor = Color.White
        ),
        navigationIcon =
        {
            if (viewConfig.showBackButton) {
                IconButton(onClick = {
                    viewConfig.onClickNavIcon()
                }) {
                    Icon(tint = Color.White, imageVector = Icons.Filled.ArrowBack, contentDescription = "back")
                }
            }
        },
        actions = {
            Content()

        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAppBar(
    text: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearch: (String) -> Unit,
) {

    val context = LocalContext.current
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        color = MaterialTheme.colorScheme.primary
    ) {
        TextField(modifier = Modifier
            .fillMaxWidth().testTag(TestTags.SearchField),
            value = text,
            onValueChange = {
                onTextChange(it)
            },
            placeholder = {
                Text(
                    modifier = Modifier
                        .alpha(0.5f),
                    text = context.getString(R.string.placeholder_search),
                    color = Color.White
                )
            },
            textStyle = TextStyle(
                fontSize = MaterialTheme.typography.titleMedium.fontSize
            ),
            singleLine = true,
            leadingIcon = {
                IconButton(
                    modifier = Modifier.testTag(LeadingSearchIcon)
                        .alpha(0.5f),
                    onClick = {}
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Leading Search Icon",
                        tint = Color.White
                    )
                }
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        if (text.isNotEmpty()) {
                            onTextChange("")
                        } else {
                            onCloseClicked()
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Icon",
                        tint = Color.White
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearch(text)
                }
            ),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                textColor = Color.White,
                cursorColor = MaterialTheme.colorScheme.onSurfaceVariant
            ))
    }
}


@Composable
@Preview
fun DefaultAppBarPreview() {
    DefaultAppBar() {
        CustomIcon(Icons.Filled.Search, Color.White)
    }
}

@Composable
@Preview
fun SearchAppBarPreview() {
    SearchAppBar(
        text = "Some random text",
        onTextChange = {},
        onCloseClicked = {},
        onSearch = {}
    )
}