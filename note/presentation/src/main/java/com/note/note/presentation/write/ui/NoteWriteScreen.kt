@file:OptIn(ExperimentalMaterial3Api::class)

package com.note.note.presentation.write.ui

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.note.core.designsystem.NoteDivider
import com.note.core.designsystem.NoteShape50
import com.note.core.designsystem.NoteTheme
import com.note.core.designsystem.components.NoteInputTextDialog
import com.note.core.designsystem.components.NoteScaffold
import com.note.core.designsystem.components.NoteToolbar
import com.note.core.designsystem.components.item.MenuItem
import com.note.core.ui.ObserveAsEvents
import com.note.note.domain.model.NoteImage
import com.note.note.domain.model.NoteLink
import com.note.note.presentation.R
import com.note.note.presentation.component.ContentImagesSection
import com.note.note.presentation.component.ContentLinksSection
import com.note.core.ui.openLink
import com.note.note.presentation.write.action.NoteWriteAction
import com.note.note.presentation.write.action.NoteWriteEvent
import com.note.note.presentation.write.state.NoteLinkUi
import com.note.note.presentation.write.state.NoteWriteMode
import com.note.note.presentation.write.state.NoteWriteState
import com.note.note.presentation.write.viewmodel.NoteWriteViewModel

@Composable
fun NoteWriteScreenRoot(
    viewModel: NoteWriteViewModel
) {

    val context = LocalContext.current

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(10)
    ) { uris ->
        val uriPathList = uris.map { it.toString() }
        viewModel.onAction(NoteWriteAction.AddImages(uriPathList))
    }

    ObserveAsEvents(flow = viewModel.event) { event ->
        when (event) {
            NoteWriteEvent.SaveComplete -> {
                showToast(context, context.getString(R.string.save_complete_note))
            }

            is NoteWriteEvent.Error -> {
                showToast(context, event.error.asString(context))
            }
        }
    }

    NoteWriteScreen(
        state = viewModel.state,
        isLoading = viewModel.isLoading,
    ) { action ->
        when (action) {

            is NoteWriteAction.OpenLink -> {
                context.openLink(action.url)
            }

            NoteWriteAction.OpenImagePicker -> {
                val mediaRequest = PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                imagePickerLauncher.launch(mediaRequest)
            }

            else -> viewModel.onAction(action)
        }
    }
}


@Composable
fun NoteWriteScreen(
    state: NoteWriteState,
    isLoading: Boolean,
    onAction: (NoteWriteAction) -> Unit
) {

    val showLinkDialog = remember { mutableStateOf(false) }
    val showBranchDialog = remember { mutableStateOf(false) }

    NoteScaffold(
        topAppBar = { NoteWriteTopAppBar(state = state, onAction = onAction) }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            NoteContentSection(
                state = state,
                onAction = onAction
            )
            NoteFooterSection(
                state = state,
                onLinkDialogRequested = { showLinkDialog.value = true },
                onBranchDialogRequested = { showBranchDialog.value = true },
                onAction = onAction
            )
        }
    }

    if(showLinkDialog.value) {
        ShowLinkDialog(
            onDismiss = { showLinkDialog.value = false },
            onConfirm = { onAction.invoke(NoteWriteAction.AddLink(it)) }
        )
    }

    if(showBranchDialog.value) {
        ShowBranchDialog(
            branchName = state.noteContent.branchName,
            onDismiss = { showBranchDialog.value = false },
            onConfirm = { onAction.invoke(NoteWriteAction.UpdateBranchName(it)) }
        )
    }

    if(isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(NoteShape50)
                .pointerInput(Unit) {}
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.Center),
                strokeWidth = 1.5.dp,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }

}

@Composable
private fun NoteWriteTopAppBar(state: NoteWriteState, onAction: (NoteWriteAction) -> Unit) {
    NoteToolbar(
        title = if (state.mode == NoteWriteMode.CREATE) {
            stringResource(id = R.string.create_note)
        } else {
            stringResource(id = R.string.edit_note)
        },
        showBackButton = true,
        menuItemEnabled = state.isSaveAble(),
        menuItems = listOf(
            MenuItem(
                title = stringResource(id = R.string.save_note),
                icon = Icons.Rounded.Done
            )
        ),
        onMenuItemClick = { onAction.invoke(NoteWriteAction.Save) },
        onBackClick = { onAction.invoke(NoteWriteAction.BackPressed) }
    )
}

@Composable
private fun ContentInputField(content: String, onAction: (NoteWriteAction) -> Unit) {
    OutlinedTextField(
        value = content,
        onValueChange = { onAction.invoke(NoteWriteAction.UpdateContent(it)) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        textStyle = MaterialTheme.typography.bodyLarge,
        placeholder = { Text(text = stringResource(R.string.content_hint)) },
        minLines = 5
    )
}
@Composable
private fun ColumnScope.NoteContentSection(
    state: NoteWriteState,
    onAction: (NoteWriteAction) -> Unit
) {
    Column(modifier = Modifier.weight(1f)) {
        ContentInputField(content = state.noteContent.content, onAction = onAction)
        ContentLinksSection(
            links = state.links,
            canDelete = true,
            onDelete = { onAction(NoteWriteAction.DeleteLink(it)) },
            onSelect = { onAction(NoteWriteAction.OpenLink(it)) }
        )
        ContentImagesSection(
            images = state.images,
            canDelete = true,
            onDelete = { onAction(NoteWriteAction.DeleteImage(it)) },
            onSelect = { onAction(NoteWriteAction.ViewImage(it)) }
        )
    }
}

@Composable
private fun NoteFooterSection(
    state: NoteWriteState,
    onLinkDialogRequested: () -> Unit,
    onBranchDialogRequested: () -> Unit,
    onAction: (NoteWriteAction) -> Unit
) {
    HorizontalDivider(color = NoteDivider)
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = { onAction(NoteWriteAction.OpenImagePicker) }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_add_image),
                contentDescription = stringResource(R.string.add_image)
            )
        }
        IconButton(onClick = onLinkDialogRequested) {
            Icon(
                painter = painterResource(id = R.drawable.ic_add_link),
                contentDescription = stringResource(R.string.add_link)
            )
        }
        Spacer(Modifier.weight(1f))
        Text(
            text = stringResource(R.string.branch_format, state.noteContent.branchName),
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.clickable { onBranchDialogRequested() }
        )
        Spacer(Modifier.size(8.dp))
        Icon(
            painter = painterResource(id = R.drawable.ic_branch),
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
private fun ShowLinkDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    NoteInputTextDialog(
        title = stringResource(id = R.string.input_link_title),
        description = stringResource(id = R.string.input_link_message),
        hint = stringResource(id = R.string.link_sample),
        confirmButtonText = stringResource(id = com.note.core.designsystem.R.string.complete),
        onDismiss = { onDismiss.invoke() },
        onConfirm = onConfirm,
    )
}

@Composable
private fun ShowBranchDialog(
    branchName: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    NoteInputTextDialog(
        title = stringResource(id = R.string.input_branch_title),
        description = stringResource(id = R.string.input_branch_message),
        hint = stringResource(id = R.string.branch_sample),
        content = branchName,
        confirmButtonText = stringResource(id = com.note.core.designsystem.R.string.edit),
        onDismiss = { onDismiss.invoke() },
        onConfirm = onConfirm,
    )
}

private fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

@Preview
@Composable
fun NoteWriteScreenPreview() {
    NoteTheme {
        NoteWriteScreen(
            state = NoteWriteState(
                links = listOf(
                    NoteLinkUi(
                        isLoading = false,
                        link = NoteLink(
                            title = stringResource(id = com.note.core.designsystem.R.string.dummy_middle),
                            link = "https://s.pstatic.net/static/www/mobile/edit/20240112_1095/upload_1705057885416AaxUM.png",
                            description = stringResource(id = com.note.core.designsystem.R.string.dummy_middle),
                            image = "https://s.pstatic.net/static/www/mobile/edit/20240112_1095/upload_1705057885416AaxUM.png"
                        )
                    )
                ),
                images = listOf(
                    NoteImage(
                        uriPath = "https://s.pstatic.net/static/www/mobile/edit/20240112_1095/upload_1705057885416AaxUM.png",
                    )
                )
            ),
            isLoading = true,
        ) {

        }
    }
}