package com.note.note.domain.usecase

import com.note.core.domain.result.DataError
import com.note.core.domain.result.Result
import com.note.note.domain.model.Note
import com.note.note.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNoteFlowUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
    operator fun invoke(noteId: Long): Flow<Result<Note?, DataError.Local>> =
        noteRepository.getNoteFlow(noteId)
}