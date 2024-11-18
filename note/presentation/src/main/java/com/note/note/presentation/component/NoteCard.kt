package com.note.note.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.note.core.designsystem.NoteCommit
import com.note.core.designsystem.NoteMerge
import com.note.core.designsystem.NoteTheme
import com.note.core.designsystem.components.NoteVerticalSpacer
import com.note.note.domain.enums.BranchState
import com.note.note.domain.model.Note
import com.note.note.presentation.R
import com.note.note.presentation.component.preview.NoteCardPreviewParameterProvider
import com.note.note.presentation.extension.getStateColor

@Composable
fun NoteCard(
    note: Note,
    onNoteClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onNoteClick() },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            NoteBranchName(branchName = note.content.branchName)
            NoteVerticalSpacer()
            NoteContentText(content = note.content.content)
            NoteVerticalSpacer()
            NoteBranchStatus(branchState = note.content.branchState)
        }
    }
}

@Composable
private fun NoteBranchName(branchName: String) {
    Text(
        text = branchName,
        style = MaterialTheme.typography.bodySmall,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun NoteContentText(content: String) {
    Text(
        text = content,
        style = MaterialTheme.typography.labelMedium,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun NoteBranchStatus(branchState: BranchState) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val color = branchState.getStateColor()
        Icon(
            painter = painterResource(id = R.drawable.ic_branch),
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = branchState.name,
            style = MaterialTheme.typography.bodyMedium,
            color = color
        )
    }
}


@Preview
@Composable
fun NoteCardPreview(
    @PreviewParameter(NoteCardPreviewParameterProvider::class) note: Note
) {
    NoteTheme {
        NoteCard(
            note = note,
        ) {

        }
    }
}