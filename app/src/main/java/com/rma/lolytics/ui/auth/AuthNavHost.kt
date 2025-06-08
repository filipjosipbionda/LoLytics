package com.rma.lolytics.ui.auth

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.rma.lolytics.ui.auth.login.LoginScreen
import com.rma.lolytics.ui.auth.password.ResetPasswordScreen
import com.rma.lolytics.ui.auth.register.RegisterScreen
import com.rma.lolytics.ui.navigation.CoreGraph
import com.rma.lolytics.ui.navigation.LoginGraph

fun NavGraphBuilder.authGraph(navController: NavHostController) {
    navigation<LoginGraph.Root>(
        startDestination = LoginGraph.Login
    ){
        composable<LoginGraph.Login> {
            LoginScreen(
                onRegister = {
                    navController.navigate(LoginGraph.Register)
                },
                onForgotPassword = {
                    navController.navigate(LoginGraph.ForgotPassword)
                },
                onLoginSuccess = { navController.navigate(CoreGraph.Root) }
            )
        }
        composable<LoginGraph.Register>{
            RegisterScreen(
                title = LoginGraph.Register.TITLE,
                navigateBack = {
                    navController.popBackStack()
                },
                onRegistrationSuccess = {
                    navController.popBackStack()
                    navController.navigate(CoreGraph.Root)
                }
            )
        }
        composable<LoginGraph.ForgotPassword> {
            ResetPasswordScreen(
                title = LoginGraph.ForgotPassword.TITLE,
                navigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}