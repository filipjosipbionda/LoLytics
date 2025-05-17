package com.rma.lolytics.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object Splash

sealed class LoginGraph(val route: String) {
    data object Root : LoginGraph("auth")
    data object Login : LoginGraph("login")
    data object Register : LoginGraph("register") {
        const val TITLE = "Register"
    }
    data object ForgotPassword : LoginGraph("forgot") {
        const val TITLE = "Reset password"
    }
}

sealed class CoreGraph(val route: String) {
    data object Root : CoreGraph("core")
    data object Home: CoreGraph("home")
}