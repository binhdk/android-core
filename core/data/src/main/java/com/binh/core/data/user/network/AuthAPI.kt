package com.binh.core.data.user.network

import com.binh.core.data.network.NetworkResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthAPI {
    @POST("user/refreshToken")
    suspend fun refreshToken(@Body refreshToken: RefreshToken): NetworkResponse<AccessToken>
}