package com.note.note.presentation.previewimage.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.flab.core.navigator.Destination
import com.flab.core.navigator.Navigator
import com.note.note.presentation.previewimage.action.PreviewImageAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreviewImageViewModel @Inject constructor(
    private val navigator: Navigator,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    val imageUri by mutableStateOf(savedStateHandle.toRoute<Destination.PreviewImageScreen>().imageUri)

    fun onAction(action: PreviewImageAction) {
        when(action) {
            PreviewImageAction.BackPressed -> navigateUp()
        }
    }

    private fun navigateUp() {
        viewModelScope.launch {
            navigator.navigateUp()
        }
    }
}