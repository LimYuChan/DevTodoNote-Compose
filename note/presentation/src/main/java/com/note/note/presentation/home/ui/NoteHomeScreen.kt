@file:OptIn(ExperimentalMaterial3Api::class)

package com.note.note.presentation.home.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.note.core.designsystem.NotePrimary
import com.note.core.designsystem.components.NoteScaffold
import com.note.core.designsystem.components.NoteToolbar
import com.note.note.domain.enums.toNoteState
import com.note.note.presentation.R
import com.note.note.presentation.home.action.NoteHomeAction
import com.note.note.presentation.home.state.NoteHomeState
import com.note.note.presentation.home.viewmodel.NoteHomeViewModel
import com.note.note.presentation.list.ui.NoteListScreenRoot
import kotlinx.coroutines.launch

@Composable
fun NoteHomeScreenRoot(
    viewModel: NoteHomeViewModel
) {
    NoteHomeScreen(
        viewModel.args.repositoryName,
        viewModel.args.repositoryId,
        onAction = { viewModel.onAction(it) }
    )
}

@Composable
fun NoteHomeScreen(
    repositoryName: String,
    repositoryId: Int,
    onAction: (NoteHomeAction) -> Unit
) {
    val tabs = listOf("All", "Todo", "Done")
    val pagerState = rememberPagerState { tabs.size }
    val coroutineScope = rememberCoroutineScope()

    NoteScaffold(
        topAppBar = {
            NoteToolbar(
                title = repositoryName,
                showBackButton = true,
                onBackClick = { onAction(NoteHomeAction.BackPressed) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onAction(NoteHomeAction.WriteNoteClick) }) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.create_note))
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            NoteTabBar(
                tabs = tabs,
                selectedTabIndex = pagerState.currentPage,
                onTabSelected = { pageIndex ->
                    coroutineScope.launch { pagerState.animateScrollToPage(pageIndex) }
                }
            )
            NotePager(
                pagerState = pagerState,
                tabs = tabs,
                repositoryId = repositoryId,
                onNoteSelected = { noteId -> onAction(NoteHomeAction.NoteClick(noteId)) }
            )
        }
    }
}

@Composable
private fun NoteTabBar(
    tabs: List<String>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    TabRow(
        selectedTabIndex = selectedTabIndex,
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                color = NotePrimary
            )
        }
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                modifier = Modifier.padding(vertical = 12.dp),
                selected = selectedTabIndex == index,
                onClick = { onTabSelected(index) }
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 12.dp),
                    style = if (selectedTabIndex == index) MaterialTheme.typography.labelMedium else MaterialTheme.typography.bodyMedium,
                    text = title
                )
            }
        }
    }
}

@Composable
private fun NotePager(
    pagerState: PagerState,
    tabs: List<String>,
    repositoryId: Int,
    onNoteSelected: (noteId: Long) -> Unit
) {
    HorizontalPager(
        state = pagerState,
        userScrollEnabled = false,
        key = { tabs[it] }
    ) { page ->
        NoteListScreenRoot(
            repositoryId = repositoryId,
            noteState = page.toNoteState()
        ) { noteId ->
            onNoteSelected(noteId)
        }
    }
}