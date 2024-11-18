package com.flab.core.navigator

import kotlinx.serialization.Serializable

sealed interface Destination {
    //--------------Graph--------------//
    @Serializable
    data object AuthGraph: Destination

    @Serializable
    data object HomeGraph: Destination

    @Serializable
    data class NoteGraph(val repositoryId: Int, val repositoryName: String): Destination

    //--------------SCREEN--------------//
    @Serializable
    data object AuthScreen: Destination

    @Serializable
    data object HomeScreen: Destination

    @Serializable
    data object NoteHomeScreen: Destination

    @Serializable
    data class NoteWriteScreen(val repositoryId: Int, val noteId: Long = -1L): Destination

    @Serializable
    data class NoteDetailScreen(val repositoryId: Int, val noteId: Long, val repositoryName: String):
        Destination

    @Serializable
    data class PreviewImageScreen(val imageUri: String): Destination
}