package com.binh.core.example.ui.user

import com.binh.core.data.onFailure
import com.binh.core.data.onSuccess
import com.binh.core.data.user.User
import com.binh.core.data.user.UserRepository
import com.binh.core.ui.BaseViewModel
import com.binh.core.ui.UiState
import com.binh.core.ui.asFailureUiState
import com.binh.core.ui.asSuccessUiState
import com.binh.core.ui.util.Event
import com.binh.core.ui.util.setEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(private val userRepository: UserRepository) :
    BaseViewModel() {

    private val _loginEvent = MutableStateFlow<Event<UiState<User>>>(Event(UiState.None))
    val loginEvent: StateFlow<Event<UiState<User>>>
        get() = _loginEvent

    fun login(email: String, password: String) {
        launch {
            userRepository.login(
                email,
                password
            ).handleUnAuthorizedFailure()
                .onFailure {
                    _loginEvent.setEvent(it.asFailureUiState)
                }
                .onSuccess { _loginEvent.setEvent(it.user.asSuccessUiState) }
        }
    }

}