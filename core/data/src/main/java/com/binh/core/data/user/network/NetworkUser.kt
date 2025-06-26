package com.binh.core.data.user.network

import com.binh.core.data.user.User
import com.binh.core.data.user.local.UserEntity
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkUser(
    val id: Long,
    val name: String,
    val email: String,
    val phone: String
)

val NetworkUser.asExternalModel: User
    get() = User(id, name, email, phone)

val NetworkUser.asEntity: UserEntity
    get() = UserEntity(id, name, email, phone)