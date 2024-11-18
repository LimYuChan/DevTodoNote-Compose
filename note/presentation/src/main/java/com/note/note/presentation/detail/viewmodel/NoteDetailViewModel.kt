package com.note.note.presentation.detail.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.flab.core.navigator.Destination
import com.flab.core.navigator.Navigator
import com.note.core.common.Logg
import com.note.core.common.extension.release
import com.note.core.domain.result.DataError
import com.note.core.domain.result.Result
import com.note.core.ui.UiText
import com.note.core.ui.asUiText
import com.note.note.domain.enums.BranchState
import com.note.note.domain.model.Note
import com.note.note.domain.usecase.DeleteNoteUseCase
import com.note.note.domain.usecase.FetchNoteEventUseCase
import com.note.note.domain.usecase.GetNoteFlowUseCase
import com.note.note.domain.usecase.GetNoteUseCase
import com.note.note.domain.usecase.UpdateBranchStateUseCase
import com.note.note.presentation.R
import com.note.note.presentation.detail.action.NoteDetailAction
import com.note.note.presentation.detail.state.NoteDetailEvent
import com.note.note.presentation.detail.state.NoteDetailState
import com.note.note.presentation.write.state.NoteLinkUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    private val getNoteFlowUseCase: GetNoteFlowUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val fetchNoteEventUseCase: FetchNoteEventUseCase,
    private val updateBranchStateUseCase: UpdateBranchStateUseCase,
    private val navigator: Navigator,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var state by mutableStateOf(NoteDetailState())
        private set

    private val args: Destination.NoteDetailScreen = savedStateHandle.toRoute()

    private val eventChannel = Channel<NoteDetailEvent>()
    val event = eventChannel.receiveAsFlow()

    private var fetchNoteEventJob: Job? = null

    init {
        loadNote()
    }


    private fun loadNote() {
        getNoteFlowUseCase.invoke(noteId = args.noteId)
            .onEach { handleLoadNoteResult(it) }
            .launchIn(viewModelScope)
    }

    private suspend fun handleLoadNoteResult(result: Result<Note?, DataError.Local>) {
        when (result) {
            is Result.Success -> {
                result.data?.let { initNoteData(it) }
                    ?: sendErrorEvent(UiText.StringResource(R.string.not_found_note))
            }
            is Result.Error -> sendErrorEvent(result.error.asUiText())
        }
    }

    private fun initNoteData(note: Note) {
        updateState {
            copy(
                branchFetching = true,
                noteContent = note.content,
                images = note.images,
                links = note.links.mapIndexed { index, noteLink ->
                    NoteLinkUi(isLoading = false, linkIndex = index, link = noteLink)
                }
            )
        }
        fetchNoteState(note.content.branchName, note.content.branchState)
    }

    private fun fetchNoteState(branchName: String, originState: BranchState){
        fetchNoteEventJob.release()
        fetchNoteEventJob = viewModelScope.launch {
            val result = fetchNoteEventUseCase.invoke(args.repositoryName, branchName)
            handleFetchNoteEventResult(result, originState)
            updateState { copy(branchFetching = false) }
        }
    }

    private suspend fun handleFetchNoteEventResult(result: Result<BranchState, DataError.Network>, originState: BranchState) {
        when(result) {
            is Result.Success -> {
                if(result.data != originState) {
                    updateBranchStateUseCase.invoke(args.noteId, result.data)
                }
            }
            is Result.Error -> sendErrorEvent(result.error.asUiText())
        }
    }

    fun onAction(action: NoteDetailAction) {
        when (action) {
            NoteDetailAction.DeleteNote -> deleteNote()
            NoteDetailAction.BackPressed -> navigateUp()
            NoteDetailAction.EditNote -> handleEditNote()
            is NoteDetailAction.ViewImage -> navigate(Destination.PreviewImageScreen(action.uri))
            else -> Unit
        }
    }


    private fun deleteNote() {
        viewModelScope.launch {
            when(val result = deleteNoteUseCase.invoke(args.noteId)) {
                is Result.Success -> handleDeleteSuccess()
                is Result.Error -> sendErrorEvent(result.error.asUiText())
            }
        }
    }

    private suspend fun handleDeleteSuccess() {
        eventChannel.send(NoteDetailEvent.DeleteComplete)
        navigateUp()
    }

    private fun handleEditNote() {
        if(state.noteContent.branchState == BranchState.TODO) {
            navigate(Destination.NoteWriteScreen(repositoryId = args.repositoryId, noteId = args.noteId))
        }else{
            viewModelScope.launch {
                sendErrorEvent(UiText.StringResource(R.string.not_available_edit))
            }
        }
    }

    private fun navigate(destination: Destination) {
        viewModelScope.launch {
            navigator.navigate(destination)
        }
    }

    private fun navigateUp() {
        viewModelScope.launch {
            navigator.navigateUp()
        }
    }


    private suspend fun sendErrorEvent(error: UiText) {
        eventChannel.send(NoteDetailEvent.Error(error))
    }

    private fun updateState(transform: NoteDetailState.() -> NoteDetailState) {
        state = state.transform()
    }

    override fun onCleared() {
        super.onCleared()
        fetchNoteEventJob.release()
    }
}