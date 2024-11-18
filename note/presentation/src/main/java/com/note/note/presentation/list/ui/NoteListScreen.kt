@file:OptIn(ExperimentalMaterial3Api::class)

package com.note.note.presentation.list.ui

import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.note.core.designsystem.components.NoteScaffold
import com.note.note.domain.enums.NoteState
import com.note.note.domain.model.Note
import com.note.note.presentation.component.NoteCard
import com.note.note.presentation.list.action.NoteListAction
import com.note.note.presentation.list.viewmodel.NoteListViewModel

@Composable
fun NoteListScreenRoot(
    repositoryId: Int,
    noteState: NoteState?,
    viewModel: NoteListViewModel = hiltViewModel(),
    onNoteClick: (Long) -> Unit
) {
    val notesFlow = remember { viewModel.getNotes(repositoryId, noteState) }
    val notes = notesFlow.collectAsLazyPagingItems()

    NoteListScreen(
        notes = notes,
        onAction = { action ->
            when (action) {
                is NoteListAction.NoteClick -> {
                    onNoteClick.invoke(action.noteId)
                }
            }
        }
    )
}

@Composable
fun NoteListScreen(
    notes: LazyPagingItems<Note>,
    onAction: (NoteListAction) -> Unit
) {
    val context = LocalContext.current
    var isRefreshing by remember { mutableStateOf(false) }
    val snackBarHostState = remember { SnackbarHostState() }

    HandleLoadState(
        loadState = notes.loadState.refresh,
        snackBarHostState = snackBarHostState,
        context = context,
        onRetry = { notes.refresh() },
        onFinishedRefresh = { isRefreshing = false }
    )

    NoteScaffold(snackBarHostState = snackBarHostState) {
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = {
                isRefreshing = true
                notes.refresh()
            }
        ) {
            NoteListContent(
                notes = notes,
                onAction = onAction
            )
        }
    }
}


@Composable
fun NoteListContent(
    notes: LazyPagingItems<Note>,
    onAction: (NoteListAction) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(
            count = notes.itemCount,
            key = { index -> notes.peek(index)?.content?.id ?: index }
        ) { index ->
            val note = notes[index]
            if (note != null) {
                NoteCard(
                    note = note,
                    onNoteClick = { onAction.invoke(NoteListAction.NoteClick(note.content.id)) }
                )
            }
        }
    }
}


@Composable
fun HandleLoadState(
    loadState: LoadState,
    snackBarHostState: SnackbarHostState,
    context: Context,
    onRetry: () -> Unit,
    onFinishedRefresh: () -> Unit
) {
    LaunchedEffect(loadState) {
        when (loadState) {
            is LoadState.Error -> {
                val result = snackBarHostState.showSnackbar(
                    message = loadState.error.localizedMessage
                        ?: context.getString(com.note.core.ui.R.string.error_message_unknown),
                    actionLabel = context.getString(com.note.core.designsystem.R.string.retry)
                )
                if (result == SnackbarResult.ActionPerformed) {
                    onRetry()
                }
            }

            is LoadState.NotLoading -> {
                onFinishedRefresh.invoke()
            }

            else -> Unit
        }
    }
}