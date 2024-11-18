package com.note.note.data.di

import com.note.note.data.repository.GithubEventRepositoryImpl
import com.note.note.data.repository.JsoupLinkMetaDataFetcher
import com.note.note.data.repository.NoteRepositoryImpl
import com.note.note.domain.repository.GithubEventRepository
import com.note.note.domain.repository.LinkMetaDataFetcher
import com.note.note.domain.repository.NoteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataModule {
    @Binds
    abstract fun bindsNoteRepository(impl: NoteRepositoryImpl): NoteRepository

    @Binds
    abstract fun bindsLinkMetaDataFetcher(impl: JsoupLinkMetaDataFetcher): LinkMetaDataFetcher

    @Binds
    abstract fun bindsRepositoryEventFetcher(impl: GithubEventRepositoryImpl): GithubEventRepository
}