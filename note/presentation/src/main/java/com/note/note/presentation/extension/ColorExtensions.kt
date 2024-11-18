package com.note.note.presentation.extension

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.note.core.designsystem.NoteCommit
import com.note.core.designsystem.NoteMerge
import com.note.note.domain.enums.BranchState


@Composable
fun BranchState.getStateColor(): Color {
    return when (this) {
        BranchState.COMMIT -> NoteCommit
        BranchState.MERGE -> NoteMerge
        else -> MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
    }
}