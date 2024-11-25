package com.note.core.data.di

import android.media.ImageReader
import com.note.core.data.auth.AuthStorageImpl
import com.note.core.data.image.ImageStorageImpl
import com.note.core.data.user.UserRepositoryImpl
import com.note.core.domain.AuthStorage
import com.note.core.domain.ImageStorage
import com.note.core.domain.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class AuthUserDataModule {

    @Binds
    abstract fun bindsAuthStorage(authStorageImpl: AuthStorageImpl): AuthStorage

    @Binds
    abstract fun bindsUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

    @Binds
    abstract fun bindsImageStorage(imageStorageImpl: ImageStorageImpl): ImageStorage
}