package com.note.note.presentation.write.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.flab.core.navigator.Destination
import com.flab.core.navigator.Navigator
import com.note.core.common.extension.release
import com.note.core.domain.result.Result
import com.note.core.ui.UiText
import com.note.core.ui.asUiText
import com.note.note.domain.model.Note
import com.note.note.domain.model.NoteContent
import com.note.note.domain.model.NoteImage
import com.note.note.domain.model.NoteLink
import com.note.note.domain.usecase.GetLastContentIdUseCase
import com.note.note.domain.usecase.GetNoteUseCase
import com.note.note.domain.usecase.ParseLinkDataUseCase
import com.note.note.domain.usecase.UpsertNoteUseCase
import com.note.note.presentation.write.action.NoteWriteAction
import com.note.note.presentation.write.action.NoteWriteEvent
import com.note.note.presentation.write.state.NoteWriteMode
import com.note.note.presentation.write.state.NoteLinkUi
import com.note.note.presentation.write.state.NoteWriteState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.Random
import javax.inject.Inject

@HiltViewModel
class NoteWriteViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getNoteUseCase: GetNoteUseCase,
    private val parseLinkUseCase: ParseLinkDataUseCase,
    private val saveNoteUseCase: UpsertNoteUseCase,
    private val getLastContentIdUseCase: GetLastContentIdUseCase,
    private val navigator: Navigator
): ViewModel() {

    private val noteInfo: Destination.NoteWriteScreen = savedStateHandle.toRoute()
    private val repositoryId: Int = noteInfo.repositoryId
    private val noteId: Long = noteInfo.noteId

    private var currentJob: Job? = null

    var state by mutableStateOf(savedStateHandle[SAVED_NOTE_STATE_KEY] ?: NoteWriteState())
        private set
    var isLoading by mutableStateOf(false)
        private set

    private val eventChannel = Channel<NoteWriteEvent>()
    val event = eventChannel.receiveAsFlow()

    init {
        val needsToLoadNote = !state.isSaveAble()
        if (needsToLoadNote) {
            loadNote()
        }
    }

    private fun loadNote() {
        launchNewJob {
            isLoading = true
            when(val result = getNoteUseCase.invoke(noteId)) {
                is Result.Success -> {
                    result.data?.let { initNoteData(it) } ?: initNetNote()
                }
                is Result.Error -> {
                    initNetNote()
                    sendErrorEvent(result.error.asUiText())
                }
            }
            isLoading = false
        }
    }

    private fun initNoteData(note: Note) {
        updateState {
            copy(
                mode = NoteWriteMode.EDIT,
                noteContent = note.content.copy(repositoryId = repositoryId),
                images = note.images,
                links = note.links.mapIndexed { index, noteLink ->
                    NoteLinkUi(isLoading = false, linkIndex = index, link = noteLink)
                }
            )
        }
    }

    private fun initNetNote(){
        savedStateHandle[SAVED_NOTE_ID_KEY] = INVALID_NOTE_ID
        updateState {
            copy(
                mode = NoteWriteMode.CREATE,
                noteContent = noteContent.copy(repositoryId = repositoryId)
            )
        }
        generateAndSetBranchName()
    }

    private fun generateAndSetBranchName() {
        launchNewJob {
            val branchNumber = getLastContentIdOrRandom()
            val branchName = String.format(Locale.getDefault(), BRANCH_NAME_TEMPLATE, branchNumber)
            updateBranchName(branchName)
        }
    }

    private suspend fun getLastContentIdOrRandom(): Long {
        return when (val result = getLastContentIdUseCase.invoke()) {
            is Result.Success -> result.data
            is Result.Error -> getRandomBranchNumber()
        }
    }


    private fun updateBranchName(branchName: String) {
        updateContent { copy(branchName = branchName) }
    }

    private fun launchNewJob(action: suspend CoroutineScope.() -> Unit) {
        currentJob.release()
        currentJob = viewModelScope.launch { action() }
    }

    fun onAction(action: NoteWriteAction) {
        when(action) {
            NoteWriteAction.Save -> saveNote()
            NoteWriteAction.BackPressed -> navigateUp()

            is NoteWriteAction.UpdateContent -> updateContent { copy(content = action.content) }
            is NoteWriteAction.UpdateBranchName -> updateBranchName(action.branchName)

            is NoteWriteAction.DeleteLink -> deleteLink(action.linkUi)
            is NoteWriteAction.AddLink -> addLink(action.url)

            is NoteWriteAction.AddImages -> addImages(action.uris)
            is NoteWriteAction.DeleteImage -> deleteImage(action.image)
            is NoteWriteAction.ViewImage -> viewImage(action.uri)
            else -> Unit
        }
    }

    private fun viewImage(uri: String) {
        viewModelScope.launch {
            navigator.navigate(Destination.PreviewImageScreen(uri))
        }
    }

    private fun deleteLink(linkUi: NoteLinkUi) {
        updateLinks { filterNot { it == linkUi } }
    }

    private fun addLink(link: String) {
        val newLinkUi = NoteLinkUi(isLoading = true, link = NoteLink())
        updateLinks { this + newLinkUi }

        viewModelScope.launch {
            when(val result = parseLinkUseCase.invoke(link)) {
                is Result.Success -> updateParseLink(result.data)
                is Result.Error -> {
                    updateLinks { dropLast(1) }
                    sendErrorEvent(result.error.asUiText())
                }
            }
        }
    }

    private fun updateParseLink(noteLink: NoteLink) {
        val updatedLinks = state.links.toMutableList()
        val lastIndex = updatedLinks.lastIndex
        updatedLinks[lastIndex] = updatedLinks[lastIndex].copy(
            isLoading = false,
            linkIndex = lastIndex,
            link = noteLink
        )
        updateLinks { updatedLinks }
    }

    private fun deleteImage(deleteImage: NoteImage) {
        updateImages { filterNot { it == deleteImage } }
    }

    private fun addImages(pathUri: List<String?>) {
        val newImages = pathUri.filterNotNull().map { NoteImage(uriPath = it) }
        updateImages { this + newImages }
    }

    private fun saveNote() {
        viewModelScope.launch {
            isLoading = true
            val result = saveNoteUseCase.invoke(state.toNote())
            isLoading = false
            when(result){
                is Result.Success -> handleSaveSuccess()
                is Result.Error -> sendErrorEvent(result.error.asUiText())
            }
        }
    }

    private suspend fun handleSaveSuccess() {
        eventChannel.send(NoteWriteEvent.SaveComplete)
        navigateUp()
    }

    private fun getRandomBranchNumber(): Long {
        return (randomGenerator.nextInt(RANDOM_BRANCH_MAX - RANDOM_BRANCH_MIN) + RANDOM_BRANCH_MIN).toLong()
    }

    private fun updateState(transform: NoteWriteState.() -> NoteWriteState) {
        state = state.transform()
        savedStateHandle[SAVED_NOTE_STATE_KEY] = state
    }

    private fun updateContent(transform: NoteContent.() -> NoteContent) {
        updateState { copy(noteContent = noteContent.transform()) }
    }

    private fun updateImages(transform: List<NoteImage>.() -> List<NoteImage>) {
        updateState { copy(images = images.transform()) }
    }

    private fun updateLinks(transform: List<NoteLinkUi>.() -> List<NoteLinkUi>) {
        updateState { copy(links = links.transform()) }
    }

    private suspend fun sendErrorEvent(error: UiText){
        eventChannel.send(NoteWriteEvent.Error(error))
    }

    private fun navigateUp() {
        viewModelScope.launch {
            navigator.navigateUp()
        }
    }

    override fun onCleared() {
        super.onCleared()
        currentJob.release()
    }

    companion object {
        private const val BRANCH_NAME_TEMPLATE: String = "WORK-%d"
        private const val SAVED_NOTE_ID_KEY = "noteId"
        private const val SAVED_NOTE_STATE_KEY = "state"
        private const val INVALID_NOTE_ID = -1L
        private const val RANDOM_BRANCH_MIN = 1000
        private const val RANDOM_BRANCH_MAX = 10000
        private val randomGenerator = Random()
    }
}