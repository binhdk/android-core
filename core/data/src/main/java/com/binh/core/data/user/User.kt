package com.binh.core.data.user

data class User(
    val id: Long,
    val name: String,
    val email: String,
    val phone: String,
    val accessToken: String = "",
    val refreshToken: String = "",
)