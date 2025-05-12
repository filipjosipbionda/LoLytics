package com.rma.lolytics.ui.di

import com.rma.lolytics.ui.auth.login.LoginViewModel
import com.rma.lolytics.ui.splash.SplashViewModel
import org.koin.dsl.module

val uiModule = module {
    single {
        SplashViewModel(authRepository = get())
    }

    single {
        LoginViewModel(
            authRepository = get()
        )
    }
}