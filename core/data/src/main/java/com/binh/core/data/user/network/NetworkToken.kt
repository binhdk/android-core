package com.binh.core.data.user.network

import com.binh.core.data.user.Token
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkToken(
    @Json(name = "access_token") val accessToken: String,
    @Json(name = "refresh_token") val refreshToken: String
)

val NetworkToken.asExternalModel get() = Token(accessToken, refreshToken)