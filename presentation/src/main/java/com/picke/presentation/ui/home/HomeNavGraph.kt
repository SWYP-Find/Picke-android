package com.picke.presentation.ui.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.picke.presentation.AppRoute
import com.picke.presentation.ui.main.BottomNavItem
import com.picke.presentation.ui.main.MainTabScreen

fun NavGraphBuilder.homeGraph(
    navController: NavController,
    onTabClick: (BottomNavItem) -> Unit,
    scrollToTopTrigger: () -> Int,
    isNotificationSheetPending: () -> Boolean
) {
    composable(BottomNavItem.Home.route) { backStackEntry ->
        MainTabScreen(
            navController = navController,
            backStackEntry = backStackEntry,
            onTabClick = onTabClick
        ) {
            HomeScreen(
                onNavigateToAlarm = {
                    navController.navigate(AppRoute.Alarm.route)
                },
                onNavigateToVote = { contentId ->
                    navController.navigate(AppRoute.BattleRouting.createRoute(contentId))
                },
                scrollToTopTrigger = scrollToTopTrigger(),
                isNotificationSheetPending = isNotificationSheetPending()
            )
        }
    }
}