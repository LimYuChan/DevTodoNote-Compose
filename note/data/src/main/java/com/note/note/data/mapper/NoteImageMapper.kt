package com.note.note.data.mapper

import com.note.core.database.entity.NoteImageEntity
import com.note.note.domain.model.NoteImage

fun NoteImageEntity.toNoteImage(): NoteImage {
    return NoteImage(
        id = id,
        contentId = content_id,
        uriPath = uri_path
    )
}

fun NoteImage.toNoteImageEntity(): NoteImageEntity {
    return NoteImageEntity(
        id = id,
        content_id = contentId,
        uri_path = uriPath
    )
}