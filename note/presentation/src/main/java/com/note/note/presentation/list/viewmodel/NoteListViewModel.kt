package com.note.note.presentation.list.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.note.note.domain.enums.NoteState
import com.note.note.domain.model.Note
import com.note.note.domain.usecase.GetNotesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getNotesUseCase: GetNotesUseCase
) : ViewModel() {

    private var savedRepositoryId: Int
        get() = savedStateHandle.get<Int>(KEY_REPOSITORY_ID) ?: -1
        set(value) {
            savedStateHandle[KEY_REPOSITORY_ID] = value
        }
    private var savedNoteState: NoteState?
        get() = savedStateHandle[KEY_NOTE_STATE]
        set(value) {
            savedStateHandle[KEY_NOTE_STATE] = value
        }




    fun getNotes(repositoryId: Int, state: NoteState?): Flow<PagingData<Note>> {
        savedRepositoryId = repositoryId
        savedNoteState = state

        return getNotesUseCase
            .invoke(savedRepositoryId, savedNoteState)
            .cachedIn(viewModelScope)
    }

    companion object {
        private const val KEY_REPOSITORY_ID = "repositoryId"
        private const val KEY_NOTE_STATE = "noteState"
    }
}