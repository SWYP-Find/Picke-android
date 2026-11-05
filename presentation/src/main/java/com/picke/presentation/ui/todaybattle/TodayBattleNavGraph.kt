package com.picke.presentation.ui.todaybattle

import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.picke.presentation.AppRoute

fun NavGraphBuilder.todayBattleGraph(navController: NavController) {
    composable(
        route = AppRoute.TodayBattle.route,
        arguments = listOf(
            navArgument("battleId") {
                type = NavType.StringType; nullable = true; defaultValue = null
            }
        )
    ) { backStackEntry ->
        val battleId = backStackEntry.arguments?.getString("battleId")
        TodayBattleScreen(
            initialBattleId = battleId,
            onBackClick = dropUnlessResumed { navController.popBackStack() },
            onNavigateToScenario = { id ->
                navController.navigate(AppRoute.Scenario.createRoute(id)) {
                    popUpTo(AppRoute.TodayBattle.route) { inclusive = true }
                }
            },
            onNavigateToPerspective = { id ->
                navController.navigate(AppRoute.Perspective.createRoute(id)) {
                    popUpTo(AppRoute.TodayBattle.route) { inclusive = true }
                }
            }
        )
    }
}