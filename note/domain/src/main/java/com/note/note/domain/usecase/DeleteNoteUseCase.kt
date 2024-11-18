package com.note.note.domain.usecase

import com.note.core.domain.result.DataError
import com.note.core.domain.result.Result
import com.note.note.domain.repository.NoteRepository
import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
    suspend operator fun invoke(noteId: Long): Result<Long, DataError.Local>{
        return noteRepository.deleteNote(noteId)
    }
}