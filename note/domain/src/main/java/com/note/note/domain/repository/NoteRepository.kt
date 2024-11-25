package com.note.note.domain.repository

import androidx.paging.PagingData
import com.note.core.domain.result.DataError
import com.note.core.domain.result.Result
import com.note.note.domain.enums.BranchState
import com.note.note.domain.enums.NoteState
import com.note.note.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    fun getNotesByRepositoryId(repositoryId: Int): Flow<PagingData<Note>>

    fun getNotesByState(repositoryId: Int, state: NoteState): Flow<PagingData<Note>>

    fun getNoteFlow(noteId: Long): Flow<Result<Note?, DataError.Local>>

    suspend fun getNote(noteId: Long): Result<Note?, DataError.Local>

    suspend fun getLastContentId(): Result<Long, DataError.Local>

    suspend fun upsertNote(note: Note): Result<Long, DataError.Local>

    suspend fun deleteNote(contentId: Long): Result<Long, DataError.Local>

    suspend fun updateBranchState(branchState: BranchState, contentId: Long, noteState: NoteState)
}