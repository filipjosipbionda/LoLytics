package com.rma.lolytics.ui.splash

import androidx.lifecycle.ViewModel
import com.rma.lolytics.data.repository.auth.AuthRepository

class SplashViewModel(
    private val authRepository: AuthRepository,
): ViewModel() {
    fun checkIfUserIsLoggedIn(): Boolean = authRepository.checkIfUserIsLogged()
}