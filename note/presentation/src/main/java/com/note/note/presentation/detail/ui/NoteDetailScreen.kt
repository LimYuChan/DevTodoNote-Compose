@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class
)

package com.note.note.presentation.detail.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.note.core.designsystem.NoteTheme
import com.note.core.designsystem.components.NoteConfirmDialog
import com.note.core.designsystem.components.NoteScaffold
import com.note.core.designsystem.components.NoteToolbar
import com.note.core.designsystem.components.item.MenuItem
import com.note.core.ui.ObserveAsEvents
import com.note.note.presentation.R
import com.note.note.presentation.component.ContentImagesSection
import com.note.note.presentation.component.ContentLinksSection
import com.note.note.presentation.detail.action.NoteDetailAction
import com.note.note.presentation.detail.state.NoteDetailEvent
import com.note.note.presentation.detail.state.NoteDetailState
import com.note.note.presentation.detail.viewmodel.NoteDetailViewModel
import com.note.core.ui.openLink
import com.note.core.ui.showToast
import com.note.note.domain.enums.BranchState
import com.note.note.presentation.extension.getStateColor

@Composable
fun NoteDetailScreenRoot(
    viewModel: NoteDetailViewModel
) {
    val context = LocalContext.current

    ObserveAsEvents(flow = viewModel.event) { event ->
        when (event) {
            NoteDetailEvent.DeleteComplete -> {
                context.showToast(context.getString(R.string.delete_complete_note))
            }

            is NoteDetailEvent.Error -> {
                context.showToast(event.error.asString(context))
            }
        }
    }

    NoteDetailScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                is NoteDetailAction.OpenLink -> context.openLink(action.url)
                else -> viewModel.onAction(action)
            }
        }
    )
}

@Composable
private fun NoteDetailScreen(
    state: NoteDetailState,
    onAction: (NoteDetailAction) -> Unit
) {

    val showDeleteDialog = remember { mutableStateOf(false) }

    NoteScaffold(
        topAppBar = {
            NoteToolbar(
                showBackButton = true,
                menuItems = listOf(
                    MenuItem(
                        title = stringResource(id = R.string.edit_note),
                        icon = Icons.Rounded.Edit
                    ),
                    MenuItem(
                        title = stringResource(id = R.string.delete_note),
                        icon = Icons.Rounded.Delete
                    )
                ),
                onMenuItemClick = { itemIndex ->
                    when (itemIndex) {
                        0 -> onAction.invoke(NoteDetailAction.EditNote)
                        1 -> showDeleteDialog.value = true
                    }
                },
                onBackClick = { onAction.invoke(NoteDetailAction.BackPressed) }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Text(
                text = state.noteContent.content,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                style = MaterialTheme.typography.bodyLarge,
                minLines = 5
            )
            BranchStateRow(state = state)
            ContentLinksSection(
                links = state.links,
                canDelete = false,
                onSelect = { onAction.invoke(NoteDetailAction.OpenLink(it)) }
            )
            ContentImagesSection(
                images = state.images,
                canDelete = false,
                onSelect = { onAction.invoke(NoteDetailAction.ViewImage(it)) }
            )
        }
    }
    when {
        showDeleteDialog.value -> {
            NoteConfirmDialog(
                title = stringResource(id = R.string.delete_note_title),
                description = stringResource(id = R.string.delete_note_description),
                onDismiss = { showDeleteDialog.value = false },
                onConfirm = { onAction.invoke(NoteDetailAction.DeleteNote) }
            )
        }
    }
}

@Composable
private fun BranchStateRow(state: NoteDetailState){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val branchStateColor = state.noteContent.branchState.getStateColor()
        Text(
            text = stringResource(R.string.branch_format, state.noteContent.branchName),
            style = MaterialTheme.typography.labelMedium,
            color = branchStateColor
        )
        Spacer(Modifier.size(8.dp))
        if(state.branchFetching){
            CircularProgressIndicator(
                modifier = Modifier
                    .size(12.dp),
                strokeWidth = 1.5.dp,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }else{
            Icon(
                painter = painterResource(id = R.drawable.ic_branch),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = branchStateColor
            )
        }
    }
}

@Preview
@Composable
fun NoteDetailScreenPreview() {
    NoteTheme {
        NoteDetailScreen(
            state = NoteDetailState(branchFetching = true),
            onAction = {}
        )
    }
}