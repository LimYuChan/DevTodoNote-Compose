package com.note.note.presentation.write.action

import com.note.note.domain.model.NoteImage
import com.note.note.presentation.write.state.NoteLinkUi

sealed interface NoteWriteAction {
    data object BackPressed: NoteWriteAction

    data object OpenImagePicker: NoteWriteAction

    data class UpdateContent(val content: String): NoteWriteAction
    data class UpdateBranchName(val branchName: String): NoteWriteAction

    data class AddLink(val url: String): NoteWriteAction
    data class DeleteLink(val linkUi: NoteLinkUi): NoteWriteAction
    data class OpenLink(val url: String): NoteWriteAction

    data class AddImages(val uris: List<String?>): NoteWriteAction
    data class DeleteImage(val image: NoteImage): NoteWriteAction
    data class ViewImage(val uri: String): NoteWriteAction

    data object Save: NoteWriteAction
}