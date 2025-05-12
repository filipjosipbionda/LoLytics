package com.rma.lolytics.ui.splash

import androidx.lifecycle.ViewModel
import com.rma.lolytics.data.repository.AuthRepository

class SplashViewModel(
    private val authRepository: AuthRepository,
): ViewModel() {
    fun checkIfUserIsLoggedIn(): Boolean = authRepository.checkIfUserIsLogged()
}