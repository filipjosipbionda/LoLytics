package com.rma.lolytics.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rma.lolytics.data.repository.AuthRepository
import com.rma.lolytics.ui.auth.login.model.AuthUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
): ViewModel() {
    private val _loginState = MutableStateFlow<AuthUiState<Unit>>(AuthUiState.Idle)
    val loginState: StateFlow<AuthUiState<Unit>> = _loginState

    fun login(
        email: String,
        password: String
    ) {
        _loginState.value = AuthUiState.Loading
        viewModelScope.launch {
            authRepository.login(
                email = email,
                password = password
            ).onSuccess {
                _loginState.value = AuthUiState.Success(Unit)
            }.onFailure {
                _loginState.value = AuthUiState.Error(it.message)
            }
        }
    }

    fun setIdleState() {
        _loginState.value = AuthUiState.Idle
    }
}
