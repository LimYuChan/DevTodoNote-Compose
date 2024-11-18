package com.note.core.database.dao

import android.util.Log
import androidx.room.Dao
import androidx.room.Transaction
import com.note.core.database.entity.NoteEntity
import com.note.core.database.entity.NoteImageEntity
import com.note.core.database.entity.NoteLinkEntity
import com.note.core.domain.ImageStorage


@Dao
interface NoteWriteDao {

    @Transaction
    suspend fun upsertNote(
        contentDao: NoteContentDao,
        imageDao: NoteImageDao,
        linkDao: NoteLinkDao,
        imageStorage: ImageStorage,
        noteEntity: NoteEntity
    ): Long {
        val upsertContentResult = contentDao.upsertContent(noteEntity.content)

        val contentId = if(noteEntity.content.id != 0L) noteEntity.content.id else upsertContentResult

        val updatedImages = noteEntity.images.map { it.copy(content_id = contentId) }
        val updatedLinks = noteEntity.links.map { it.copy(content_id = contentId) }

        // 이미지와 링크 비교 후 필요한 데이터만 갱신
        updateImages(imageDao, imageStorage, contentId, updatedImages)
        updateLinks(linkDao, contentId, updatedLinks)

        return contentId
    }

    @Transaction
    suspend fun deleteNote(
        contentDao: NoteContentDao,
        imageDao: NoteImageDao,
        linkDao: NoteLinkDao,
        contentId: Long
    ): Long {
        contentDao.deleteContent(contentId)
        imageDao.deleteImagesByContentId(contentId)
        linkDao.deleteLinksByContentId(contentId)

        return contentId
    }

    private suspend fun updateImages(
        imageDao: NoteImageDao,
        imageStorage: ImageStorage,
        contentId: Long,
        updatedImages: List<NoteImageEntity>
    ) {
        val currentImages = imageDao.getImagesByContentId(contentId)

        val imagesToDelete = currentImages.filterNot { it in updatedImages }
        val imagesToInsert = updatedImages.filterNot { it in currentImages }.map {
            it.copy(uri_path = imageStorage.copyImageToInternalStorage(it.uri_path))
        }

        imageDao.deleteImages(imagesToDelete)
        imageDao.upsertImages(imagesToInsert)
    }

    private suspend fun updateLinks(
        linkDao: NoteLinkDao,
        contentId: Long,
        updatedLinks: List<NoteLinkEntity>
    ) {
        val currentLinks = linkDao.getLinksByContentId(contentId)

        val linksToDelete = currentLinks.filterNot { it in updatedLinks }
        val linksToInsert = updatedLinks.filterNot { it in currentLinks }

        linkDao.deleteLinks(linksToDelete)
        linkDao.upsertLinks(linksToInsert)
    }
}