package com.binh.core.data.user.network

import com.binh.core.data.user.Token
import com.binh.core.data.user.User
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkLoginResponse(val user: NetworkUser, val token: NetworkToken)

val NetworkLoginResponse.asExternalModel
    get() = LoginResponse(
        user.asExternalModel,
        token.asExternalModel
    )

data class LoginResponse(val user: User, val token: Token)