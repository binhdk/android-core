package com.binh.core.data.network

import com.binh.core.data.BuildConfig
import com.binh.core.data.user.AccessTokenProvider
import com.binh.core.data.user.OnTokenRefreshed
import com.binh.core.data.user.RefreshTokenProvider
import com.binh.core.data.util.NetworkAvailableProvider
import com.binh.core.data.util.getMoshiBuilder
import okhttp3.OkHttpClient
import org.jetbrains.annotations.TestOnly
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitProvider {

    private val retrofit: Retrofit =
        Retrofit
            .Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(
                MoshiConverterFactory.create(getMoshiBuilder())
            )
            .addConverterFactory(StringConverterFactory())
            .addConverterFactory(NullOnEmptyConverterFactory())
            .build()

    fun get(
        httpClient: OkHttpClient,
    ): Retrofit = retrofit.newBuilder()
        .client(httpClient)
        .build()


    @TestOnly
    fun getCommonRetrofit(networkAvailableProvider: NetworkAvailableProvider): Retrofit =
        get(HttpClientProvider.getCommonHttpClient(networkAvailableProvider))

    @TestOnly
    fun getAuthRetrofit(
        accessTokenProvider: AccessTokenProvider,
        refreshTokenProvider: RefreshTokenProvider,
        networkAvailableProvider: NetworkAvailableProvider,
        refreshTokenCallback: OnTokenRefreshed
    ): Retrofit =
        get(
            HttpClientProvider.getAuthHttpClient(
                accessTokenProvider,
                refreshTokenProvider,
                networkAvailableProvider,
                refreshTokenCallback
            )
        )

    @TestOnly
    fun getFileTransferRetrofit(
        accessTokenProvider: AccessTokenProvider,
        refreshTokenProvider: RefreshTokenProvider,
        networkAvailableProvider: NetworkAvailableProvider,
        refreshTokenCallback: OnTokenRefreshed
    ): Retrofit =
        get(
            HttpClientProvider.getFileTransferHttpClient(
                accessTokenProvider,
                refreshTokenProvider,
                networkAvailableProvider,
                refreshTokenCallback
            )
        )

}