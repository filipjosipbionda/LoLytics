package com.rma.lolytics.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object Splash

@Serializable
sealed class LoginGraph {
    abstract val route: String

    @Serializable
    data object Root : LoginGraph() {
        override val route = "auth"
    }

    @Serializable
    data object Login : LoginGraph() {
        override val route = "login"
    }

    @Serializable
    data object Register : LoginGraph() {
        override val route = "register"
        const val TITLE = "Register"
    }

    @Serializable
    data object ForgotPassword : LoginGraph() {
        override val route = "forgot"
        const val TITLE = "Reset password"
    }
}

@Serializable
sealed class CoreGraph {
    @Serializable
    data object Root : CoreGraph()

    @Serializable
    data object Home : CoreGraph()

    @Serializable
    data object Champions : CoreGraph()

    @Serializable
    data class ProfileSettings(val profilePictureUrl: String?) : CoreGraph()
}
