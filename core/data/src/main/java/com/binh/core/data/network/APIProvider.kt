package com.binh.core.data.network

import com.binh.core.data.user.AccessTokenProvider
import com.binh.core.data.user.OnTokenRefreshed
import com.binh.core.data.user.RefreshTokenProvider
import com.binh.core.data.util.NetworkAvailableProvider
import org.jetbrains.annotations.TestOnly
import retrofit2.create

@TestOnly
class APIProvider(
    accessTokenProvider: AccessTokenProvider,
    refreshTokenProvider: RefreshTokenProvider,
    refreshTokenCallback: OnTokenRefreshed,
    networkAvailableProvider: NetworkAvailableProvider
) {

    val commonOkHttpClient = HttpClientProvider.getCommonHttpClient(networkAvailableProvider)

    val commonRetrofit = RetrofitProvider.getCommonRetrofit(networkAvailableProvider)

    val authRetrofit = RetrofitProvider.getAuthRetrofit(
        accessTokenProvider,
        refreshTokenProvider,
        networkAvailableProvider,
        refreshTokenCallback
    )

    val fileTransferRetrofit = RetrofitProvider.getFileTransferRetrofit(
        accessTokenProvider,
        refreshTokenProvider,
        networkAvailableProvider,
        refreshTokenCallback
    )


    inline fun <reified T> createService(): T {
        return commonRetrofit.create(T::class.java)
    }

    inline fun <reified T> createAuthorizedService(): T {
        return authRetrofit.create(T::class.java)
    }

    inline fun <reified T> createFileTransferService(): T {
        return fileTransferRetrofit.create(T::class.java)
    }

    inline fun <reified T> createService(headers: Map<String, String>? = null): T {
        return headers?.let { headers ->
            commonRetrofit.newBuilder()
                .client(commonOkHttpClient.newBuilder().addInterceptor { chain ->
                    val newRequest = chain.request()
                    headers.entries.forEach { newRequest.newBuilder().header(it.key, it.value) }
                    chain.proceed(newRequest)
                }.build()).build().create()
        } ?: createService()
    }
}
