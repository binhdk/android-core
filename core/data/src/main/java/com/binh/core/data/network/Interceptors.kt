package com.binh.core.data.network

import android.util.Log
import com.auth0.android.jwt.JWT
import com.binh.core.data.BuildConfig
import com.binh.core.data.user.AccessTokenProvider
import com.binh.core.data.user.OnTokenRefreshed
import com.binh.core.data.user.RefreshTokenProvider
import com.binh.core.data.user.network.AccessTokenResponse
import com.binh.core.data.user.network.RefreshToken
import com.binh.core.data.util.NetworkAvailableProvider
import com.binh.core.data.util.jsonToObject
import com.binh.core.data.util.toJson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import java.util.concurrent.atomic.AtomicInteger


const val AUTHORIZATION_HEADER = "Authorization"
const val AUTHORIZATION_TYPE = ""

class AuthInterceptor(private val tokenProvider: AccessTokenProvider) :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val origin: Request = chain.request()
        val builder: Request.Builder = origin.newBuilder()
        val token = tokenProvider.invoke()
        if (token.isNotBlank())
            builder.header(AUTHORIZATION_HEADER, AUTHORIZATION_TYPE + token)
        else
            builder.removeHeader(AUTHORIZATION_HEADER)

        return chain.proceed(builder.build())
    }

}


class NetworkConnectionInterceptor(private val isNetworkAvailable: NetworkAvailableProvider) :
    Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isNetworkAvailable()) {
            throw NoConnectivityException()
            // Throwing our custom exception 'NoConnectivityException'
        }
        val builder = chain.request().newBuilder()
        return chain.proceed(builder.build())
    }
}

class NoConnectivityException : IOException("No Internet Connection")


class RefreshTokenAuthenticator(
    private val okHttpClient: OkHttpClient,
    private val accessToken: AccessTokenProvider,
    private val refreshToken: RefreshTokenProvider,
    private val onTokenRefreshed: OnTokenRefreshed
) : Authenticator {

    private val maxRetryCount: Int = 3

    private val retryCount: AtomicInteger = AtomicInteger(0)


    /**
     * make method Synchronized ensure multi thread waiting for 1 refresh token processed
     * */
    @Synchronized
    override fun authenticate(route: Route?, response: Response): Request? {
        // Refresh your access_token using a synchronous api request

        val requestedToken =
            response.request.header(AUTHORIZATION_HEADER)?.split(' ')
                ?.get(if (AUTHORIZATION_TYPE.isBlank()) 0 else 1)?.trim()

        // if token refreshed, just get stored token
        val storedAccessToken = accessToken.invoke()
        val isStoreAccessTokenValid = try {
            JWT(storedAccessToken)
            true
        } catch (e: Exception) {
            Log.e(TAG, "e", e)
            false
        }
        if (requestedToken != storedAccessToken && isStoreAccessTokenValid) {
            return response.request.newBuilder()
                .header(AUTHORIZATION_HEADER, AUTHORIZATION_TYPE + storedAccessToken)
                .build()
        }

        // start refresh token
        // prevent refresh token if multiple retry failure
        if (retryCount.incrementAndGet() >= maxRetryCount) {
            retryCount.set(0)
            throw RefreshTokenUnknownException(Throwable("Too many refresh token call"))
        }

        return try {
            val result =
                okHttpClient.newCall(
                    Request.Builder()
                        .url(BuildConfig.BASE_URL + "user/refreshToken")
                        .addHeader(
                            AUTHORIZATION_HEADER,
                            AUTHORIZATION_TYPE + refreshToken.invoke()
                        )
                        .post(
                            RefreshToken(refreshToken.invoke()).toJson()
                            !!.toRequestBody("application/json".toMediaTypeOrNull())
                        )
                        .build()
                ).execute()

            //refresh token invalid
            if (result.code in arrayOf(401, 403)) {
                retryCount.set(0)
                throw RefreshTokenUnAuthorizedException()
            }

            result.body.string().let {
                val newAccessToken = it.jsonToObject<AccessTokenResponse>()!!.data.accessToken


                onTokenRefreshed.invoke(
                    newAccessToken,
                    refreshToken.invoke()
                )

                retryCount.decrementAndGet()
                // Add new header to rejected request and retry it
                response.request.newBuilder()
                    .header(AUTHORIZATION_HEADER, AUTHORIZATION_TYPE + newAccessToken)
                    .build()
            }
        } catch (e: RefreshTokenUnAuthorizedException) {
            retryCount.set(0)
            Log.e(TAG, "unauthorized refresh token", e)
            throw e
        } catch (e: Exception) {
            retryCount.decrementAndGet()
            Log.e(TAG, "refresh token failure", e)
            throw RefreshTokenUnknownException(e)
        }
    }

    companion object {
        const val TAG = "RefreshTokenAuthenticator"
    }
}

class RefreshTokenUnAuthorizedException : IOException("Unauthorized refresh token")
class RefreshTokenUnknownException(e: Throwable) : IOException("Refresh token unknown error", e)


fun getHttpLoggingInterceptor(): Interceptor = HttpLoggingInterceptor().apply {
    level = when (BuildConfig.DEBUG) {
        true -> HttpLoggingInterceptor.Level.BODY
        else -> HttpLoggingInterceptor.Level.NONE
    }
}

fun getAuthInterceptor(accessTokenProvider: AccessTokenProvider): Interceptor =
    AuthInterceptor(accessTokenProvider)

fun getNetworkConnectionInterceptor(networkAvailableProvider: NetworkAvailableProvider): Interceptor =
    NetworkConnectionInterceptor(networkAvailableProvider)

fun getRefreshTokenAuthenticator(
    okHttpClient: OkHttpClient,
    accessTokenProvider: AccessTokenProvider,
    refreshTokenProvider: RefreshTokenProvider,
    onTokenRefreshed: OnTokenRefreshed
): Authenticator =
    RefreshTokenAuthenticator(
        okHttpClient,
        accessTokenProvider,
        refreshTokenProvider,
        onTokenRefreshed,
    )
