package com.note.githubtodo.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.flab.core.navigator.Destination
import com.note.auth.presentation.ui.AuthScreenRoot
import com.note.core.common.Logg
import com.note.home.presentation.ui.HomeScreenRoot
import com.note.note.presentation.detail.ui.NoteDetailScreenRoot
import com.note.note.presentation.detail.viewmodel.NoteDetailViewModel
import com.note.note.presentation.home.ui.NoteHomeScreenRoot
import com.note.note.presentation.home.viewmodel.NoteHomeViewModel
import com.note.note.presentation.write.ui.NoteWriteScreenRoot
import com.note.note.presentation.write.viewmodel.NoteWriteViewModel
import com.note.note.presentation.previewimage.ui.PreviewImageScreenRoot
import com.note.note.presentation.previewimage.viewmodel.PreviewImageViewModel

@Composable
fun NavigationRoot(
    navController: NavHostController,
    startDestination: Destination,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        authGraph(navController)
        homeGraph()
        noteGraph(navController)
    }
}

private fun NavGraphBuilder.authGraph(navController: NavHostController) {
    navigation<Destination.AuthGraph>(
        startDestination = Destination.AuthScreen
    ) {
        composable<Destination.AuthScreen> {
            AuthScreenRoot(navController = navController)
        }
    }
}


private fun NavGraphBuilder.homeGraph() {
    navigation<Destination.HomeGraph>(
        startDestination = Destination.HomeScreen
    ) {
        composable<Destination.HomeScreen> {
            HomeScreenRoot()
        }
    }
}

private fun NavGraphBuilder.noteGraph(navController: NavHostController) {
    navigation<Destination.NoteGraph>(
        startDestination = Destination.NoteHomeScreen,
    ) {
        composable<Destination.NoteHomeScreen> { backStackEntry ->
            NoteHomeScreenRoot(viewModel = hiltViewModel(backStackEntry))
        }

        composable<Destination.NoteWriteScreen> { backStackEntry ->
            NoteWriteScreenRoot(viewModel = hiltViewModel(backStackEntry))
        }

        composable<Destination.NoteDetailScreen> { backStackEntry ->
            NoteDetailScreenRoot(viewModel = hiltViewModel(backStackEntry))
        }

        composable<Destination.PreviewImageScreen> { backStackEntry ->
            PreviewImageScreenRoot(viewModel = hiltViewModel(backStackEntry))
        }
    }
}

