package com.picke.presentation.ui.explore

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.picke.presentation.AppRoute
import com.picke.presentation.analytics.ContentActionType
import com.picke.presentation.analytics.rememberAnalyticsTracker
import com.picke.presentation.ui.main.BottomNavItem
import com.picke.presentation.ui.main.MainTabScreen

fun NavGraphBuilder.exploreGraph(
    navController: NavController,
    onTabClick: (BottomNavItem) -> Unit,
    scrollToTopTrigger: () -> Int
) {
    composable(BottomNavItem.Explore.route) { backStackEntry ->
        val analyticsTracker = rememberAnalyticsTracker()
        MainTabScreen(
            navController = navController,
            backStackEntry = backStackEntry,
            onTabClick = onTabClick
        ) {
            ExploreScreen(
                scrollToTopTrigger = scrollToTopTrigger(),
                onNavigateToAlarm = {
                    navController.navigate(AppRoute.Alarm.route)
                },
                onNavigateToVote = { battleId ->
                    analyticsTracker.trackContentAction(ContentActionType.BATTLE_CARD_TAP, battleId)
                    navController.navigate(AppRoute.BattleRouting.createRoute(battleId))
                }
            )
        }
    }
}