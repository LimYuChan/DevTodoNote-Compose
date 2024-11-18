package com.note.note.domain.usecase

import com.note.note.domain.enums.BranchState
import com.note.note.domain.enums.NoteState
import com.note.note.domain.enums.toNoteState
import com.note.note.domain.repository.NoteRepository
import javax.inject.Inject

class UpdateBranchStateUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(noteId: Long, branchState: BranchState) {
        return repository.updateBranchState(branchState, noteId, branchState.toNoteState())
    }
}