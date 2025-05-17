package com.rma.lolytics.ui.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rma.lolytics.data.repository.AuthRepository
import com.rma.lolytics.ui.auth.login.model.AuthUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {
    private val _registerState: MutableStateFlow<AuthUiState<*>> =
        MutableStateFlow(AuthUiState.Idle)
    val registerState: StateFlow<AuthUiState<*>> = _registerState

    fun register(
        email: String,
        password: String,
    ) {
        _registerState.value = AuthUiState.Loading
        viewModelScope.launch {
            authRepository.register(
                email = email,
                password = password
            ).onSuccess {
                _registerState.value = AuthUiState.Success(Unit)
            }.onFailure {
                _registerState.value = AuthUiState.Error(it.message)
            }
        }
    }

    fun setIdleState() {
        _registerState.value = AuthUiState.Idle
    }
}
