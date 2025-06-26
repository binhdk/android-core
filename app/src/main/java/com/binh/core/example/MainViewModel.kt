package com.binh.core.example

import com.binh.core.data.onFailure
import com.binh.core.data.onSuccess
import com.binh.core.data.user.User
import com.binh.core.domain.user.CurrentUserUseCase
import com.binh.core.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val currentUserUseCase: CurrentUserUseCase) :
    BaseViewModel() {

    private var _user = MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()

    private var _uiState = MutableStateFlow<MainUiState>(MainUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        launch {
            delay(2000L)
            _uiState.value = MainUiState.Success
        }
        getUser()
    }

    private fun getUser() {
        launch {
            currentUserUseCase.invoke()
                .onSuccess {
                    _user.value = it
                }.onFailure {
                    _user.value = null
                }
        }
    }
}

sealed interface MainUiState {
    data object Loading : MainUiState
    data object Success : MainUiState
}