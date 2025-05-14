package com.rma.lolytics.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rma.lolytics.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
): ViewModel() {
    private val _loginState = MutableStateFlow<LoginUiState<Unit>>(LoginUiState.Idle)
    val loginState: StateFlow<LoginUiState<Unit>> = _loginState

    fun login(
        email: String,
        password: String
    ) {
        _loginState.value = LoginUiState.Loading
        viewModelScope.launch {
            authRepository.login(
                email = email,
                password = password
            ).onSuccess {
                _loginState.value = LoginUiState.Success(Unit)
            }.onFailure {
                _loginState.value = LoginUiState.Error(it.message)
            }
        }
    }

    fun setIdleState() {
        _loginState.value = LoginUiState.Idle
    }
}