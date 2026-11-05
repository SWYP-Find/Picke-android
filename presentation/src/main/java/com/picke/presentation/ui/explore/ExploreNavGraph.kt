package com.picke.presentation.ui.explore

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.picke.presentation.analytics.ContentActionType
import com.picke.presentation.analytics.rememberAnalyticsTracker
import com.picke.presentation.navigation.AppRoute
import com.picke.presentation.navigation.BottomNavItem
import com.picke.presentation.navigation.TabScreen

fun NavGraphBuilder.exploreGraph(
    navController: NavController,
    onTabClick: (BottomNavItem) -> Unit,
    scrollToTopTrigger: () -> Int
) {
    composable(BottomNavItem.Explore.route) { backStackEntry ->
        val analyticsTracker = rememberAnalyticsTracker()
        TabScreen(
            backStackEntry = backStackEntry,
            onTabClick = onTabClick
        ) { contentModifier ->
            ExploreScreen(
                onNavigateToAlarm = {
                    navController.navigate(AppRoute.Alarm.route)
                },
                onNavigateToVote = { battleId ->
                    analyticsTracker.trackContentAction(ContentActionType.BATTLE_CARD_TAP, battleId)
                    navController.navigate(AppRoute.BattleRouting.createRoute(battleId))
                },
                modifier = contentModifier,
                scrollToTopTrigger = scrollToTopTrigger()
            )
        }
    }
}