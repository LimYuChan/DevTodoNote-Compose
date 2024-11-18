package com.note.note.presentation.write.state

import com.note.note.domain.model.Note
import com.note.note.domain.model.NoteContent
import com.note.note.domain.model.NoteImage
import kotlinx.serialization.Serializable

@Serializable
data class NoteWriteState(
    val mode: NoteWriteMode = NoteWriteMode.CREATE,
    val noteContent: NoteContent = NoteContent(),
    val links: List<NoteLinkUi> = emptyList(),
    val images: List<NoteImage> = emptyList()
): java.io.Serializable {
    fun toNote(): Note {
        return Note(
            content = noteContent,
            links = links.map { it.link },
            images = images
        )
    }

    fun isSaveAble(): Boolean {
        return noteContent.content.isNotBlank() || links.isNotEmpty() || images.isNotEmpty()
    }
}
