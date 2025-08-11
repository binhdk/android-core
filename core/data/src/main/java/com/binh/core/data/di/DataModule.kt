package com.binh.core.data.di

import android.content.Context
import com.binh.core.data.di.qualifiers.AccessTokenQualifier
import com.binh.core.data.di.qualifiers.RefreshTokenQualifier
import com.binh.core.data.user.AccessTokenProvider
import com.binh.core.data.user.OnTokenRefreshed
import com.binh.core.data.user.RefreshedTokenCallback
import com.binh.core.data.user.RefreshTokenProvider
import com.binh.core.data.user.UserSessionManager
import com.binh.core.data.util.NetworkAvailableProvider
import com.binh.core.data.util.isNetworkAvailable
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @AccessTokenQualifier
    @Provides
    fun provideAccessTokenProvider(userSessionManager: UserSessionManager): AccessTokenProvider {
        return userSessionManager::getAccessToken
    }

    @RefreshTokenQualifier
    @Provides
    fun provideRefreshTokenProvider(userSessionManager: UserSessionManager): RefreshTokenProvider {
        return userSessionManager::getRefreshToken
    }


    @Provides
    fun provideRefreshTokenCallback(refreshedTokenCallback: RefreshedTokenCallback): OnTokenRefreshed {
        return refreshedTokenCallback::onTokenRefreshed
    }

    @Provides
    fun provideIsNetworkAvailable(@ApplicationContext context: Context): NetworkAvailableProvider =
        context::isNetworkAvailable
}