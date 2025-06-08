package com.rma.lolytics.ui.core

import androidx.activity.compose.LocalActivity
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.rma.lolytics.ui.core.home.HomeScreen
import com.rma.lolytics.ui.core.match.AddMatchScreen
import com.rma.lolytics.ui.core.profile.ProfileSettingScreen
import com.rma.lolytics.ui.navigation.CoreGraph
import com.rma.lolytics.ui.navigation.LoginGraph
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute

fun NavGraphBuilder.coreGraph(navController: NavHostController) {
    navigation<CoreGraph.Root>(
        startDestination = CoreGraph.Home,
    ) {
        composable<CoreGraph.Home> {
            val activity = LocalActivity.current

            HomeScreen(
                onBackPress = {
                    if (navController.previousBackStackEntry == null) {
                        activity?.finish()
                    } else {
                        navController.popBackStack()
                    }
                },
                addMatch = {
                    navController.navigate(CoreGraph.Champions)
                },
                logout = {
                    navController.popBackStack(LoginGraph.Root.route, inclusive = false)
                    navController.navigate(LoginGraph.Root)
                },
                openProfileSettings = { profilePictureUrl ->
                    navController.navigate(CoreGraph.ProfileSettings(profilePictureUrl))
                }
            )
        }

        composable<CoreGraph.Champions> {
            AddMatchScreen(
                navigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable<CoreGraph.ProfileSettings> {
            val profileSettings: CoreGraph.ProfileSettings = it.toRoute()
            ProfileSettingScreen(
                profilePictureUrl = profileSettings.profilePictureUrl,
                navigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
