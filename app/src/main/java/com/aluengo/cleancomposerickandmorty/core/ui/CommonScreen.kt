package com.aluengo.cleancomposerickandmorty.core.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class ViewConfig(
    val showBackButton: Boolean = false,
    val onClick: (String) -> Unit = {},
    val onClickNavIcon: () -> Unit = {},
    val onSearchClicked: (() -> Unit)? = null,
    val onFABClick: () -> Unit = {},
    val fabImage: ImageVector? = null,
    val fabIsVisible: Boolean = false,
    val title: String? = null,
    val appbarBackgroundColor: Color? = null,
)

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ScaffoldView(
    viewConfig: ViewConfig = ViewConfig(),
    searchWidgetState: SearchWidgetState = SearchWidgetState.CLOSED,
    searchTextState: String = "",
    onTextChange: (String) -> Unit = {},
    onCloseClicked: () -> Unit = {},
    onSearch: (String) -> Unit = {},
    snackbarHostState: SnackbarHostState? = null,
    AppBarContent: @Composable () -> Unit = {},
    Content: @Composable (PaddingValues) -> Unit = {},
) {
    Scaffold(
        topBar = {
            MyTopAppBar(
                viewConfig,
                searchWidgetState = searchWidgetState,
                searchTextState = searchTextState,
                onTextChange = onTextChange,
                onCloseClicked = onCloseClicked,
                onSearch = onSearch,
                Content = AppBarContent
            )
        },
        snackbarHost = { snackbarHostState?.let { SnackbarHost(it) } },
    ) { contentPadding ->
        Content(contentPadding)
    }
}

@Composable
fun CustomIcon(
    icon: ImageVector? = null,
    color: Color = Color.White,
    resource: Int? = null,
    size: Dp = 25.dp,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    IconButton(
        modifier = modifier,
        onClick = {
            onClick?.invoke()
        }
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = icon.name,
                tint = color
            )
        } else if (resource != null) {
            Icon(
                painterResource(id = resource),
                contentDescription = "contentDescription",
                tint = color,
                modifier = Modifier.size(size)
            )
        }
    }
}

@Composable
fun HorizontalSpacer(height: Int = 4) {
    Spacer(modifier = Modifier.width(height.dp))
}

@Composable
fun VerticalSpacer(height: Int = 4) {
    Spacer(modifier = Modifier.height(height.dp))
}
