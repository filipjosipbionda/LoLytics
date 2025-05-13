package com.rma.lolytics.ui.auth.password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rma.lolytics.data.repository.AuthRepository
import com.rma.lolytics.ui.auth.login.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ResetPasswordViewModel(
   private val authRepository: AuthRepository,
): ViewModel() {
    private val _passwordResetUiState: MutableStateFlow<LoginUiState<*>> =
        MutableStateFlow(LoginUiState.Idle)
    val passwordResetUiState = _passwordResetUiState

    internal fun sendPasswordResetEmail(email: String) {
        viewModelScope.launch {
            authRepository
                .sendPasswordResetEmail(email)
                .onSuccess {
                    _passwordResetUiState.value = LoginUiState.Success(Unit)
                }.onFailure {
                    _passwordResetUiState.value = LoginUiState.Error(it.message)
                 }
        }
    }

    internal fun setIdleState() {
        _passwordResetUiState.value = LoginUiState.Idle
    }
}