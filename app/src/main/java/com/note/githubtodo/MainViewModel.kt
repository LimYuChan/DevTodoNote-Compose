package com.note.githubtodo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.note.core.domain.AuthStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authStorage: AuthStorage
): ViewModel() {

    var state by mutableStateOf(MainState())
        private set

    init {
        viewModelScope.launch {
            updateState { copy(isCheckingAuth = true) }
//            updateState { copy(isLoggedIn = authStorage.get()?.isValid() == true) }
            updateState { copy(isCheckingAuth = false) }
        }
    }

    private fun updateState(update: MainState.() -> MainState) {
        state = state.update()
    }
}