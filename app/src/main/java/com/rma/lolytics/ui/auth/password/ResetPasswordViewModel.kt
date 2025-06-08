package com.rma.lolytics.ui.auth.password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rma.lolytics.data.repository.auth.AuthRepository
import com.rma.lolytics.ui.auth.login.model.AuthUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ResetPasswordViewModel(
    private val authRepository: AuthRepository,
): ViewModel() {
    private val _passwordResetUiState: MutableStateFlow<AuthUiState<*>> =
        MutableStateFlow(AuthUiState.Idle)
    val passwordResetUiState: StateFlow<AuthUiState<*>> = _passwordResetUiState

    internal fun sendPasswordResetEmail(email: String) {
        viewModelScope.launch {
            authRepository
                .sendPasswordResetEmail(email)
                .onSuccess {
                    _passwordResetUiState.value = AuthUiState.Success(Unit)
                }.onFailure {
                    _passwordResetUiState.value = AuthUiState.Error(it.message)
                 }
        }
    }

    internal fun setIdleState() {
        _passwordResetUiState.value = AuthUiState.Idle
    }
}
