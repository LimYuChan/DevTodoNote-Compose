package com.note.note.presentation.detail.state

import com.note.note.domain.model.NoteContent
import com.note.note.domain.model.NoteImage
import com.note.note.presentation.write.state.NoteLinkUi

data class NoteDetailState(
    val branchFetching: Boolean = false,
    val noteContent: NoteContent = NoteContent(),
    val images: List<NoteImage> = emptyList(),
    val links: List<NoteLinkUi> = emptyList()
)
