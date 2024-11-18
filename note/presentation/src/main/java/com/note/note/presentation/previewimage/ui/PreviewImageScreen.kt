@file:OptIn(ExperimentalMaterial3Api::class)

package com.note.note.presentation.previewimage.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.note.core.designsystem.components.NoteScaffold
import com.note.core.designsystem.components.NoteToolbar
import com.note.note.presentation.R
import com.note.note.presentation.previewimage.action.PreviewImageAction
import com.note.note.presentation.previewimage.viewmodel.PreviewImageViewModel
import com.note.note.presentation.previewimage.component.ZoomableImage

@Composable
fun PreviewImageScreenRoot(
    viewModel: PreviewImageViewModel
) {
    PreviewImageScreen(
        imageUri = viewModel.imageUri,
        onAction = { viewModel.onAction(it) }
    )
}

@Composable
fun PreviewImageScreen(
    imageUri: String,
    onAction: (PreviewImageAction) -> Unit
) {
    NoteScaffold(
        topAppBar = {
            NoteToolbar(
                title = stringResource(id = R.string.view_image),
                showBackButton = true,
                onBackClick = { onAction.invoke(PreviewImageAction.BackPressed) }
            )
        }
    ) { innerPadding ->
        ZoomableImage(
            imageUri = imageUri,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

