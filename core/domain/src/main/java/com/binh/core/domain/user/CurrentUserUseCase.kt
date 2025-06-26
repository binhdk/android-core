package com.binh.core.domain.user

import com.binh.core.data.Result
import com.binh.core.data.user.User
import com.binh.core.data.user.UserRepository
import javax.inject.Inject

class CurrentUserUseCase @Inject constructor(private val userRepository: UserRepository) {

    suspend operator fun invoke(): Result<User> {
        return userRepository.getCurrentUser()
    }
}