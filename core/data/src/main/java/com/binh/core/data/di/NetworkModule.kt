package com.binh.core.data.di

import com.binh.core.data.di.qualifiers.AccessTokenQualifier
import com.binh.core.data.di.qualifiers.AuthHttpClient
import com.binh.core.data.di.qualifiers.AuthInterceptorQualifier
import com.binh.core.data.di.qualifiers.AuthRetrofit
import com.binh.core.data.di.qualifiers.CommonHttpClient
import com.binh.core.data.di.qualifiers.CommonRetrofit
import com.binh.core.data.di.qualifiers.FileTransferHttpClient
import com.binh.core.data.di.qualifiers.FileTransferRetrofit
import com.binh.core.data.di.qualifiers.LoggingInterceptorQualifier
import com.binh.core.data.di.qualifiers.NetworkConnectionInterceptorQualifier
import com.binh.core.data.di.qualifiers.RefreshTokenQualifier
import com.binh.core.data.network.HttpClientProvider
import com.binh.core.data.network.RetrofitProvider
import com.binh.core.data.network.createService
import com.binh.core.data.network.getAuthInterceptor
import com.binh.core.data.network.getHttpLoggingInterceptor
import com.binh.core.data.network.getNetworkConnectionInterceptor
import com.binh.core.data.network.getRefreshTokenAuthenticator
import com.binh.core.data.user.AccessTokenProvider
import com.binh.core.data.user.OnTokenRefreshed
import com.binh.core.data.user.RefreshTokenProvider
import com.binh.core.data.user.network.UserAPI
import com.binh.core.data.util.NetworkAvailableProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    // interceptor
    @LoggingInterceptorQualifier
    @Provides
    @Singleton
    fun providerLoggingInterceptor(): Interceptor = getHttpLoggingInterceptor()

    @AuthInterceptorQualifier
    @Provides
    @Singleton
    @JvmSuppressWildcards
    fun providerAuthInterceptor(@AccessTokenQualifier accessTokenProvider: AccessTokenProvider): Interceptor =
        getAuthInterceptor(accessTokenProvider)

    @NetworkConnectionInterceptorQualifier
    @Provides
    @Singleton
    @JvmSuppressWildcards
    fun providerNetworkConnectionInterceptor(networkAvailableProvider: NetworkAvailableProvider): Interceptor =
        getNetworkConnectionInterceptor(networkAvailableProvider)

    // authenticators
    @Singleton
    @Provides
    @JvmSuppressWildcards
    fun provideRefreshTokenAuthenticator(
        @CommonHttpClient okHttpClient: OkHttpClient,
        @AccessTokenQualifier accessTokenProvider: AccessTokenProvider,
        @RefreshTokenQualifier refreshTokenProvider: RefreshTokenProvider,
        onTokenRefreshed: OnTokenRefreshed,
    ): Authenticator =
        getRefreshTokenAuthenticator(
            okHttpClient,
            accessTokenProvider,
            refreshTokenProvider,
            onTokenRefreshed,
        )

    // http client
    @CommonHttpClient
    @Provides
    @Singleton
    fun provideCommonHttpClient(
        @LoggingInterceptorQualifier loggingInterceptor: Interceptor,
        @NetworkConnectionInterceptorQualifier networkConnectionInterceptor: Interceptor
    ): OkHttpClient =
        HttpClientProvider.get(listOf(loggingInterceptor, networkConnectionInterceptor))

    @AuthHttpClient
    @Provides
    @Singleton
    fun provideAuthHttpClient(
        @LoggingInterceptorQualifier loggingInterceptor: Interceptor,
        @AuthInterceptorQualifier authInterceptor: Interceptor,
        @NetworkConnectionInterceptorQualifier networkConnectionInterceptor: Interceptor,
        authenticator: Authenticator
    ): OkHttpClient =
        HttpClientProvider.get(
            listOf(
                loggingInterceptor,
                networkConnectionInterceptor,
                authInterceptor
            ), authenticator
        )

    @FileTransferHttpClient
    @Provides
    @Singleton
    fun provideFileTransferHttpClient(
        @LoggingInterceptorQualifier loggingInterceptor: Interceptor,
        @AuthInterceptorQualifier authInterceptor: Interceptor,
        @NetworkConnectionInterceptorQualifier networkConnectionInterceptor: Interceptor,
        authenticator: Authenticator
    ): OkHttpClient = HttpClientProvider.getFileTransfer(
        listOf(
            loggingInterceptor,
            networkConnectionInterceptor,
            authInterceptor
        ),
        authenticator
    )

    // retrofit
    @Singleton
    @Provides
    @CommonRetrofit
    fun provideCommonRetrofit(
        @CommonHttpClient httpClient: OkHttpClient
    ): Retrofit = RetrofitProvider.get(httpClient)

    @Singleton
    @Provides
    @AuthRetrofit
    fun provideAuthRetrofit(
        @AuthHttpClient httpClient: OkHttpClient
    ): Retrofit = RetrofitProvider.get(httpClient)

    @Singleton
    @Provides
    @FileTransferRetrofit
    fun provideFileTransferRetrofit(
        @FileTransferHttpClient httpClient: OkHttpClient
    ): Retrofit = RetrofitProvider.get(httpClient)

    @Provides
    fun provideUserAPI(@AuthRetrofit retrofit: Retrofit): UserAPI = retrofit.createService()
}