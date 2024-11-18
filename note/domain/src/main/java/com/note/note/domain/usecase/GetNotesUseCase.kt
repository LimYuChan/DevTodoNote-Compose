package com.note.note.domain.usecase

import android.util.Log
import androidx.paging.Logger
import androidx.paging.PagingData
import com.note.note.domain.enums.NoteState
import com.note.note.domain.model.Note
import com.note.note.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNotesUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    operator fun invoke(repositoryId: Int, state: NoteState?): Flow<PagingData<Note>> {
        return if(state == null) {
            repository.getNotesByRepositoryId(repositoryId)
        }else {
            repository.getNotesByState(repositoryId, state)
        }
    }
}