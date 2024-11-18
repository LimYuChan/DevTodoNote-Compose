package com.note.core.domain

interface ImageStorage {
    suspend fun copyImageToInternalStorage(pathUri: String): String
}