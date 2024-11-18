package com.note.note.presentation.component.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.note.note.domain.enums.BranchState
import com.note.note.domain.model.Note
import com.note.note.domain.model.NoteContent
import com.note.note.domain.model.NoteImage
import com.note.note.domain.model.NoteLink

class NoteCardPreviewParameterProvider: PreviewParameterProvider<Note> {
    override val values: Sequence<Note> = sequenceOf(
        Note(content = NoteContent(
            branchName = "test",
            content="ssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss",
            branchState = BranchState.COMMIT
        ), emptyList<NoteImage>(), emptyList<NoteLink>()),

        Note(content = NoteContent(
            branchName = "test2",
            content="ssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss",
            branchState = BranchState.MERGE
        ), emptyList<NoteImage>(), emptyList<NoteLink>()),

        Note(content = NoteContent(
            branchName = "test",
            content="ssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss",
            branchState = BranchState.TODO
        ), emptyList<NoteImage>(), emptyList<NoteLink>())
    )
}