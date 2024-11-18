package com.note.note.data.di

import com.note.note.data.service.GithubEventService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    @Singleton
    fun providesRepositoryEventService(retrofit: Retrofit): GithubEventService {
        return retrofit.create(GithubEventService::class.java)
    }
}