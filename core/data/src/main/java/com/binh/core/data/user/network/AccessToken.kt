package com.binh.core.data.user.network

import com.binh.core.data.network.NetworkResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AccessToken(@Json(name = "access_token") val accessToken: String)

@JsonClass(generateAdapter = true)
data class AccessTokenResponse(val accessToken: AccessToken) :
    NetworkResponse<AccessToken>(accessToken)

