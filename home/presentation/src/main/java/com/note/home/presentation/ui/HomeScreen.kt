@file:OptIn(ExperimentalMaterial3Api::class)

package com.note.home.presentation.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.note.core.designsystem.NoteTheme
import com.note.core.designsystem.components.NoteScaffold
import com.note.core.designsystem.components.NoteToolbar
import com.note.home.domain.exception.UnauthorizedException
import com.note.home.domain.model.Repository
import com.note.home.presentation.R
import com.note.home.presentation.action.HomeAction
import com.note.home.presentation.component.RepositoryCard
import com.note.home.presentation.viewmodel.HomeViewModel

@Composable
fun HomeScreenRoot(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val repositories = viewModel.repositories.collectAsLazyPagingItems()
    HomeScreen(
        repositories = repositories,
        onAction = { viewModel.onAction(it) }
    )
}

@Composable
fun HomeScreen(
    repositories: LazyPagingItems<Repository>,
    onAction: (HomeAction) -> Unit
){
    val context = LocalContext.current
    var isRefreshing by remember { mutableStateOf(false) }
    val snackBarHostState = remember { SnackbarHostState() }

    HandleLoadState(
        loadState = repositories.loadState.refresh,
        snackBarHostState = snackBarHostState,
        context = context,
        onUnauthorized = { onAction(HomeAction.Unauthorized) },
        onRetry = { repositories.refresh() },
        onFinishedRefresh = { isRefreshing = false }
    )

    NoteScaffold(
        topAppBar = {
            NoteToolbar(title = stringResource(id = R.string.home))
        },
        snackBarHostState = snackBarHostState
    ){ innerPadding ->
        PullToRefreshBox(
            modifier = Modifier.padding(innerPadding),
            isRefreshing = isRefreshing,
            onRefresh = {
                isRefreshing = true
                repositories.refresh()
            }
        ) {
            RepositoryList(repositories = repositories) { id, name ->
                onAction(HomeAction.RepositoryClick(id, name))
            }
        }
    }
}

@Composable
fun HandleLoadState(
    loadState: LoadState,
    snackBarHostState: SnackbarHostState,
    context: Context,
    onUnauthorized: () -> Unit,
    onRetry: () -> Unit,
    onFinishedRefresh: () -> Unit
) {
    LaunchedEffect(loadState) {

        when(loadState) {
            is LoadState.Error -> {
                if (loadState.error is UnauthorizedException) {
                    Toast.makeText(context, context.getString(R.string.unauthorized), Toast.LENGTH_SHORT).show()
                    onUnauthorized()
                } else {
                    val result = snackBarHostState.showSnackbar(
                        message = loadState.error.localizedMessage ?: context.getString(com.note.core.ui.R.string.error_message_unknown),
                        actionLabel = context.getString(com.note.core.designsystem.R.string.retry)
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        onRetry()
                    }
                }
            }
            is LoadState.NotLoading -> {
                onFinishedRefresh.invoke()
            }
            else -> Unit
        }
    }
}

@Composable
fun RepositoryList(
    repositories: LazyPagingItems<Repository>,
    onRepositoryClick: (Int, String) -> Unit
) {
    LazyColumn(
        userScrollEnabled = true,
        modifier = Modifier.fillMaxWidth()
    ) {
        items(repositories.itemCount) { index ->
            val repository = repositories[index]
            if (repository != null) {
                RepositoryCard(item = repository) {
                    onRepositoryClick(repository.id, repository.name)
                }
            }
        }
    }
}


@Preview
@Composable
fun HomeScreenPreview() {
    NoteTheme {
        HomeScreenRoot()
    }
}