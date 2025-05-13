package com.rma.lolytics.ui.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.rma.lolytics.ui.auth.login.LoginScreen
import com.rma.lolytics.ui.auth.password.ResetPasswordScreen
import com.rma.lolytics.ui.navigation.CoreGraph
import com.rma.lolytics.ui.navigation.LoginGraph

fun NavGraphBuilder.authGraph(navController: NavHostController) {
    navigation(
        startDestination = LoginGraph.Login.route,
        route = LoginGraph.Root.route
    ) {
        composable(LoginGraph.Login.route) {
            LoginScreen(
                onRegister = {
                    navController.navigate(LoginGraph.Register.route)
                },
                onForgotPassword = {
                    navController.navigate(LoginGraph.ForgotPassword.route)
                },
                onLoginSuccess = {
                    navController.navigate(CoreGraph.Root.route)
                }
            )
        }
        composable(LoginGraph.Register.route) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Register screen"
                )
            }
        }
        composable(LoginGraph.ForgotPassword.route) {
            ResetPasswordScreen(
                navigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}