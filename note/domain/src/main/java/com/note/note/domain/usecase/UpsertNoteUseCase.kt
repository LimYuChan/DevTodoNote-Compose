package com.note.note.domain.usecase

import android.util.Log
import com.note.core.domain.result.DataError
import com.note.core.domain.result.Result
import com.note.note.domain.model.Note
import com.note.note.domain.repository.NoteRepository
import javax.inject.Inject

class UpsertNoteUseCase @Inject constructor(
    private val noteRepository: NoteRepository
){
    suspend operator fun invoke(note: Note): Result<Long, DataError.Local> {
        val now = System.currentTimeMillis()

        val noteCreatedAt = if(note.content.createdAt == 0L) now else note.content.createdAt

        val resultNoteContent = note.content.copy(
            createdAt = noteCreatedAt,
            updatedAt = now
        )

        val resultNote = note.copy(
            content = resultNoteContent
        )
        return noteRepository.upsertNote(resultNote)
    }
}