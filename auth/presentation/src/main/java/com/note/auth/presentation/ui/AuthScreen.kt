package com.note.auth.presentation.ui

import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.util.Consumer
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.note.auth.presentation.R
import com.note.auth.presentation.action.AuthAction
import com.note.auth.presentation.action.AuthEvent
import com.note.auth.presentation.state.AuthState
import com.note.auth.presentation.viewmodel.AuthViewModel
import com.note.core.designsystem.NoteTheme
import com.note.core.designsystem.components.NoteBackground
import com.note.core.designsystem.components.NoteOutlinedActionButton
import com.note.core.designsystem.components.NoteVerticalSpacer
import com.note.core.designsystem.components.SpacerSize
import com.note.core.ui.ObserveAsEvents
import com.note.core.ui.openLink
import com.note.core.ui.showToast
import kotlinx.coroutines.flow.Flow

@Composable
fun AuthScreenRoot(
    viewModel: AuthViewModel = hiltViewModel(),
    navController: NavController,
) {
    val context = LocalContext.current
    val activity = context as ComponentActivity

    ObserveAsEvents(flow = viewModel.event) { event ->
        when (event) {
            is AuthEvent.Error -> {
                context.showToast(event.error.asString(context))
            }
            is AuthEvent.LaunchLoginUri -> {
                context.openLink(event.uri.toString())
            }
        }
    }

    ManageNewIntent(activity = activity, navController = navController, viewModel = viewModel)

    AuthScreen(
        state = viewModel.state,
        onAction = { viewModel.onAction(it) }
    )
}

@Composable
private fun HandleAuthEvents(
    context: Context,
    flow: Flow<AuthEvent>
) {
    ObserveAsEvents(flow = flow) { event ->
        when (event) {
            is AuthEvent.Error -> {
                context.showToast(event.error.asString(context))
            }
            is AuthEvent.LaunchLoginUri -> {
                context.openLink(event.uri.toString())
            }
        }
    }
}

@Composable
private fun ManageNewIntent(
    activity: ComponentActivity,
    navController: NavController,
    viewModel: AuthViewModel
) {
    DisposableEffect(key1 = activity, key2 = navController) {
        val onNewIntentConsumer = Consumer<Intent> { intent ->
            intent.data?.let { uri ->
                val code = uri.getQueryParameter("code")
                val state = uri.getQueryParameter(AuthViewModel.AUTH_RESULT_STATE_KEY)
                viewModel.onAction(AuthAction.GetAccessToken(state, code))
            }
        }
        activity.addOnNewIntentListener(onNewIntentConsumer)

        onDispose {
            activity.removeOnNewIntentListener(onNewIntentConsumer)
        }
    }
}

@Composable
fun AuthScreen(
    state: AuthState,
    onAction: (AuthAction) -> Unit
) {
    NoteBackground {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 180.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.auth_title),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )
                NoteVerticalSpacer()

                Text(
                    text = stringResource(id = R.string.auth_description),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center
                )
            }


            NoteOutlinedActionButton(
                text = stringResource(id = R.string.login_with_github),
                isLoading = state.isLoggingIn,
                enabled = !state.isLoggingIn,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 80.dp),
                onClick = {
                    onAction.invoke(AuthAction.LoginClick)
                }
            )
        }
    }
}

@Preview
@Composable
fun AuthScreenPreview() {
    NoteTheme {
        AuthScreen(
            state = AuthState(isLoggingIn = false)
        ) {

        }
    }
}