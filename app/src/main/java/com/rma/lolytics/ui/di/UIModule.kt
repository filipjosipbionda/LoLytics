package com.rma.lolytics.ui.di

import com.rma.lolytics.ui.auth.login.LoginViewModel
import com.rma.lolytics.ui.auth.password.ResetPasswordViewModel
import com.rma.lolytics.ui.auth.register.RegisterViewModel
import com.rma.lolytics.ui.core.home.HomeViewModel
import com.rma.lolytics.ui.core.match.AddMatchViewModel
import com.rma.lolytics.ui.core.profile.ProfileSettingsViewModel
import com.rma.lolytics.ui.splash.SplashViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    viewModel {
        SplashViewModel(authRepository = get())
    }

    viewModel {
        LoginViewModel(
            authRepository = get()
        )
    }

    viewModel {
        ResetPasswordViewModel(
            authRepository = get()
        )
    }

    viewModel {
        RegisterViewModel(
            authRepository = get()
        )
    }

    viewModel {
        HomeViewModel(
            authRepository = get(),
            firestoreRepository = get(),
            dDragonRepository = get(),
        )
    }

    viewModel {
        AddMatchViewModel(
            dragonRepository = get(),
            firestoreRepository = get(),
        )
    }

    viewModel {
        ProfileSettingsViewModel(
            firestoreRepository = get(),
        )
    }
}
