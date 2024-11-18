package com.note.auth.presentation.viewmodel

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flab.core.navigator.Destination
import com.flab.core.navigator.Navigator
import com.note.auth.domain.usecase.LoginUseCase
import com.note.auth.presentation.BuildConfig
import com.note.auth.presentation.R
import com.note.auth.presentation.action.AuthAction
import com.note.auth.presentation.action.AuthEvent
import com.note.auth.presentation.state.AuthState
import com.note.core.common.extension.release
import com.note.core.domain.result.DataError
import com.note.core.domain.result.EmptyResult
import com.note.core.domain.result.Result
import com.note.core.ui.UiText
import com.note.core.ui.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val navigator: Navigator,
    private val loginUseCase: LoginUseCase,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private var currentLoginStateKey: String
        get() = savedStateHandle[KEY_STATE_KEY] ?: ""
        set(value) {
            savedStateHandle[KEY_STATE_KEY] = value
        }

    var state by mutableStateOf(AuthState())
        private set

    private val eventChannel = Channel<AuthEvent>()
    val event = eventChannel.receiveAsFlow()

    private var loginJob: Job? = null

    fun onAction(action: AuthAction) {
        when(action) {
            is AuthAction.LoginClick -> setupLoginUri()
            is AuthAction.GetAccessToken -> getAccessToken(action.stateKey, action.code)
        }
    }

    private fun setupLoginUri() {
        val loginUri = buildLoginUri(generateRandomStateKey())
        sendEvent(AuthEvent.LaunchLoginUri(loginUri))
    }

    private fun buildLoginUri(stateKey: String): Uri {
        return Uri.Builder()
            .scheme("https")
            .authority(BuildConfig.AUTH_HOST)
            .appendPath("login")
            .appendPath("oauth")
            .appendPath("authorize")
            .appendQueryParameter(AUTH_RESULT_STATE_KEY, stateKey)
            .appendQueryParameter("scope", "repo, read:user")
            .appendQueryParameter("client_id", BuildConfig.GITHUB_CLIENT_ID)
            .appendQueryParameter("redirect_uri", BuildConfig.AUTH_REDIRECT_URI)
            .build()
    }


    private fun generateRandomStateKey(): String {
        val charset = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..STATE_KEY_LENGTH)
            .map { charset.random() }
            .joinToString("")
            .also { currentLoginStateKey = it }
    }


    private fun getAccessToken(stateKey: String?, code: String?) {
        if(stateKey != currentLoginStateKey || code.isNullOrEmpty()){
            sendEvent(AuthEvent.Error(UiText.StringResource(R.string.error_message_not_match_statekey)))
            updateState { copy(isLoggingIn = false) }
            return
        }

        loginJob.release()
        loginJob = viewModelScope.launch {
            updateState { copy(isLoggingIn = true) }
            val result = loginUseCase.invoke(code)
            handleLoginResult(result)
            updateState { copy(isLoggingIn = false) }
        }
    }

    private suspend fun handleLoginResult(result: EmptyResult<DataError.Network>) {
        when(result) {
            is Result.Success -> handleLoginSuccess()
            is Result.Error -> sendEvent(AuthEvent.Error(result.error.asUiText()))
        }
    }

    private suspend fun handleLoginSuccess() {
        navigator.navigate(
            Destination.HomeGraph,
            navOptions = {
                popUpTo(Destination.HomeGraph) {
                    inclusive = true
                }
            }
        )
    }

    private fun updateState(update: AuthState.() -> AuthState) {
        state = state.update()
    }

    private fun sendEvent(event: AuthEvent) {
        eventChannel.trySend(event)
    }

    override fun onCleared() {
        super.onCleared()
        loginJob.release()
    }


    companion object {
        private const val STATE_KEY_LENGTH = 32

        private const val KEY_STATE_KEY = "state"
        const val AUTH_RESULT_STATE_KEY = "state"
    }
}