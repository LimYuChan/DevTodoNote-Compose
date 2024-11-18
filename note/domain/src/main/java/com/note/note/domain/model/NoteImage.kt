package com.note.note.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class NoteImage(
    val id: Long = 0,
    val contentId: Long = 0,
    val uriPath: String
): java.io.Serializable