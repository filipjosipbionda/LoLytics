package com.rma.lolytics.ui.auth.login.model

sealed class AuthUiState<out T> {
    data object Idle : AuthUiState<Nothing>()
    data object Loading : AuthUiState<Nothing>()
    data class Success<out T>(val data: T) : AuthUiState<T>()
    data class Error(val message: String?) : AuthUiState<Nothing>()
}
