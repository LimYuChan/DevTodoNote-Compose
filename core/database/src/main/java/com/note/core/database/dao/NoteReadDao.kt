package com.note.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.note.core.database.entity.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteReadDao {

    @Transaction
    @Query("SELECT * FROM note_content WHERE repository_id = :repositoryId ORDER BY updated_at DESC")
    fun getNotesByRepositoryId(repositoryId: Int): PagingSource<Int, NoteEntity>

    @Transaction
    @Query("SELECT * FROM note_content WHERE repository_id = :repositoryId AND state = :status ORDER BY updated_at DESC")
    fun getNotesByStatus(repositoryId: Int, status: Int): PagingSource<Int, NoteEntity>

    @Transaction
    @Query("SELECT * FROM note_content WHERE id = :noteId")
    fun getNoteFlow(noteId: Long): Flow<NoteEntity?>

    @Transaction
    @Query("SELECT * FROM note_content WHERE id = :noteId")
    suspend fun getNote(noteId: Long): NoteEntity?
}