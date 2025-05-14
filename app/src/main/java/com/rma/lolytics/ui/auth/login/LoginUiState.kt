package com.rma.lolytics.ui.auth.login

sealed class LoginUiState<out T> {
    data object Idle : LoginUiState<Nothing>()
    data object Loading : LoginUiState<Nothing>()
    data class Success<out T>(val data: T) : LoginUiState<T>()
    data class Error(val message: String?) : LoginUiState<Nothing>()
}