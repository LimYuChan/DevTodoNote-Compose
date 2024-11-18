package com.note.note.domain.usecase

import com.note.core.domain.result.DataError
import com.note.core.domain.result.Result
import com.note.note.domain.model.Note
import com.note.note.domain.repository.NoteRepository
import javax.inject.Inject

class GetNoteUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
   suspend operator fun invoke(noteId: Long): Result<Note?, DataError.Local> {
       return noteRepository.getNote(noteId)
   }
}