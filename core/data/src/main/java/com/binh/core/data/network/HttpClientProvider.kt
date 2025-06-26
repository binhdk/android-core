package com.binh.core.data.network

import com.binh.core.data.user.AccessTokenProvider
import com.binh.core.data.user.OnTokenRefreshed
import com.binh.core.data.user.RefreshTokenProvider
import com.binh.core.data.util.CONNECTION_TIMEOUT
import com.binh.core.data.util.NetworkAvailableProvider
import com.binh.core.data.util.READ_FILE_TIMEOUT
import com.binh.core.data.util.READ_TIMEOUT
import com.binh.core.data.util.WRITE_FILE_TIMEOUT
import com.binh.core.data.util.WRITE_TIMEOUT
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.jetbrains.annotations.TestOnly
import java.util.concurrent.TimeUnit

object HttpClientProvider {

    private val httpClient: OkHttpClient =
        OkHttpClient
            .Builder()
            .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
            .build()

    fun get(
        interceptors: List<Interceptor>,
    ) = httpClient.newBuilder()
        .apply {
            interceptors.forEach { addInterceptor(it) }
        }
        .build()

    fun get(
        interceptors: List<Interceptor>,
        authenticator: Authenticator
    ) =
        httpClient.newBuilder()
            .apply {
                interceptors.forEach { addInterceptor(it) }
            }.authenticator(authenticator)
            .build()

    fun getFileTransfer(
        interceptors: List<Interceptor>,
        authenticator: Authenticator
    ) = httpClient.newBuilder()
        .apply {
            interceptors.forEach { addInterceptor(it) }
        }.authenticator(authenticator)
        .readTimeout(READ_FILE_TIMEOUT, TimeUnit.MILLISECONDS)
        .writeTimeout(WRITE_FILE_TIMEOUT, TimeUnit.MILLISECONDS)
        .build()


    @TestOnly
    fun getCommonHttpClient(networkAvailableProvider: NetworkAvailableProvider): OkHttpClient =
        get(
            listOf(
                getHttpLoggingInterceptor(),
                getNetworkConnectionInterceptor(networkAvailableProvider)
            )
        )

    @TestOnly
    fun getAuthHttpClient(
        accessTokenProvider: AccessTokenProvider,
        refreshTokenProvider: RefreshTokenProvider,
        networkAvailableProvider: NetworkAvailableProvider,
        onTokenRefreshed: OnTokenRefreshed
    ): OkHttpClient = get(
        listOf(
            getHttpLoggingInterceptor(),
            getNetworkConnectionInterceptor(networkAvailableProvider),
            getAuthInterceptor(accessTokenProvider)
        ),
        getRefreshTokenAuthenticator(
            getCommonHttpClient(networkAvailableProvider),
            accessTokenProvider,
            refreshTokenProvider,
            onTokenRefreshed
        )
    )

    @TestOnly
    fun getFileTransferHttpClient(
        accessTokenProvider: AccessTokenProvider,
        refreshTokenProvider: RefreshTokenProvider,
        networkAvailableProvider: NetworkAvailableProvider,
        onTokenRefreshed: OnTokenRefreshed
    ): OkHttpClient = getFileTransfer(
        listOf(
            getHttpLoggingInterceptor(),
            getNetworkConnectionInterceptor(networkAvailableProvider),
            getAuthInterceptor(accessTokenProvider)
        ),
        getRefreshTokenAuthenticator(
            getCommonHttpClient(networkAvailableProvider),
            accessTokenProvider,
            refreshTokenProvider,
            onTokenRefreshed
        )
    )
}