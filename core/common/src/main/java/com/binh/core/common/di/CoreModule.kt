package com.binh.core.common.di

import com.binh.core.common.AppExecutors
import com.binh.core.common.BaseDispatcherProvider
import com.binh.core.common.DispatcherProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CoreModule {

    @Singleton
    @Binds
    abstract fun bindDispatcherProvider(dispatcherProvider: DispatcherProvider): BaseDispatcherProvider

    companion object {

        @Singleton
        @Provides
        fun provideAppExecutors(): AppExecutors {
            return AppExecutors()
        }
    }

}