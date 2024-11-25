package com.note.home.data.di

import com.note.home.data.HomeService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object ServiceModule {

    @Provides
    @Singleton
    fun providesUserDataService(retrofit: Retrofit): HomeService {
        return retrofit.create(HomeService::class.java)
    }
}