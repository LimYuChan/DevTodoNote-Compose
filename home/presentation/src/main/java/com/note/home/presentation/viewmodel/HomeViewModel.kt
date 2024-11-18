package com.note.home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.flab.core.navigator.Destination
import com.flab.core.navigator.Navigator
import com.note.home.domain.model.Repository
import com.note.home.domain.usecase.GetRepositoriesUseCase
import com.note.home.presentation.action.HomeAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val navigator: Navigator,
    getRepositoriesUseCase: GetRepositoriesUseCase
): ViewModel() {

    val repositories: Flow<PagingData<Repository>> = getRepositoriesUseCase.invoke().cachedIn(viewModelScope)

    fun onAction(action: HomeAction) {
        when(action) {
            is HomeAction.Unauthorized -> navigateAuth()
            is HomeAction.RepositoryClick -> navigateNote(action)
        }
    }

    private fun navigateAuth() {
        viewModelScope.launch {
            navigator.navigate(
                destination = Destination.AuthGraph,
                navOptions = {
                    popUpTo(Destination.HomeGraph) {
                        inclusive = true
                    }
                }
            )
        }
    }

    private fun navigateNote(action: HomeAction.RepositoryClick) {
        viewModelScope.launch {
            navigator.navigate(Destination.NoteGraph(action.repositoryId, action.repositoryName))
        }
    }
}