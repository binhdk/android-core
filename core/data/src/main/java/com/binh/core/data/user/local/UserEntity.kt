package com.binh.core.data.user.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.binh.core.data.user.User

@Entity("user")
data class UserEntity(
    @PrimaryKey
    val id: Long,
    val name: String,
    val email: String,
    val phone: String,
    var accessToken: String = "",
    var refreshToken: String = "",
)

val UserEntity.asExternalModel: User
    get() = User(id, name, email, phone, accessToken, refreshToken)