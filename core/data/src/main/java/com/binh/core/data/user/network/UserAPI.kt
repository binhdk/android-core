package com.binh.core.data.user.network

import com.binh.core.data.network.NetworkResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserAPI {
    @POST("user/login")
    suspend fun login(@Body loginRequest: LoginRequest): NetworkResponse<NetworkLoginResponse>

    @GET("user/profile")
    suspend fun getCurrentUserProfile(): NetworkResponse<NetworkUser>

    @POST("user/logout")
    suspend fun logout()
}