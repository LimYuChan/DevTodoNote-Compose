package com.note.note.presentation.home.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.flab.core.navigator.Destination
import com.flab.core.navigator.Navigator
import com.note.note.presentation.home.action.NoteHomeAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteHomeViewModel @Inject constructor(
    private val navigator: Navigator,
    savedStateHandle: SavedStateHandle
): ViewModel(){

    val args: Destination.NoteGraph = savedStateHandle.toRoute()

    fun onAction(action: NoteHomeAction) {
        when(action) {
            NoteHomeAction.BackPressed -> navigateUp()
            NoteHomeAction.WriteNoteClick -> navigateToNoteWrite()
            is NoteHomeAction.NoteClick -> navigateNoteDetail(action.noteId)
        }
    }

    private fun navigateUp() {
        viewModelScope.launch {
            navigator.navigateUp()
        }
    }

    private fun navigateToNoteWrite() {
        viewModelScope.launch {
            navigator.navigate(Destination.NoteWriteScreen(args.repositoryId))
        }
    }

    private fun navigateNoteDetail(noteId: Long) {
        viewModelScope.launch {
            navigator.navigate(
                destination = Destination.NoteDetailScreen(repositoryId = args.repositoryId, noteId = noteId, repositoryName = args.repositoryName)
            )
        }
    }
}