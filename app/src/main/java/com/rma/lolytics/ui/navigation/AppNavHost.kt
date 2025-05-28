package com.rma.lolytics.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rma.lolytics.ui.auth.authGraph
import com.rma.lolytics.ui.core.coreGraph
import com.rma.lolytics.ui.splash.SplashScreen

@Composable
internal fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Splash
    ) {
        composable<Splash> {
            SplashScreen(
                onNavigateToHome = {
                    navController.popBackStack()
                    navController.navigate(CoreGraph.Home)
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                    navController.navigate(LoginGraph.Root)
                }
            )
        }
        authGraph(navController)

        coreGraph(navController)
    }
}