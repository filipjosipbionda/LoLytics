package com.rma.lolytics.ui.core

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.rma.lolytics.ui.navigation.CoreGraph

fun NavGraphBuilder.coreGraph(navController: NavHostController) {
    navigation(
        startDestination = CoreGraph.Home.route,
        route = CoreGraph.Root.route
    ) {
        composable(CoreGraph.Home.route) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                BackHandler {

                }
                Text(
                    text = "HomeScreen"
                )
            }
        }
    }
}