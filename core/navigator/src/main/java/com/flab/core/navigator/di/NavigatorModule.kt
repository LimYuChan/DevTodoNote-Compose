package com.flab.core.navigator.di

import android.util.Log
import com.flab.core.navigator.DefaultNavigator
import com.flab.core.navigator.Destination
import com.flab.core.navigator.Navigator
import com.note.core.domain.AuthStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NavigatorModule {

    @Provides
    @Singleton
    fun providesNavigator(authStorage: AuthStorage): Navigator {
        val startDestination = if (authStorage.get()?.isValid() == true) {
            Destination.HomeGraph
        } else {
            Destination.AuthGraph
        }
        return DefaultNavigator(startDestination = startDestination)
    }
}