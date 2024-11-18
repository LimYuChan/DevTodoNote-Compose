package com.note.core.designsystem.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.note.core.designsystem.NoteShape

@Composable
fun NoteScaffold(
    modifier: Modifier = Modifier,
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
    topAppBar: @Composable () -> Unit = {},
    bottomAppBar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit = {},
) {

    Scaffold(
        topBar = topAppBar,
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
                snackbar = { snackbarData ->
                    Snackbar(
                        snackbarData = snackbarData,
                        containerColor = NoteShape
                    )
                }
            )
        },
        bottomBar = bottomAppBar,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = FabPosition.EndOverlay,
        modifier = modifier
    ) { padding ->
        NoteBackground {
            content(padding)
        }
    }

}