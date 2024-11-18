package com.note.home.presentation.action

sealed interface HomeAction {
    data class RepositoryClick(val repositoryId: Int, val repositoryName: String): HomeAction
    data object Unauthorized: HomeAction
}