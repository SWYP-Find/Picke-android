package com.picke.presentation.navigation

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController

private val bottomBarTabByRoute = mapOf(
    BottomNavItem.Home.route to BottomNavItem.Home,
    BottomNavItem.Explore.route to BottomNavItem.Explore,
    BottomNavItem.My.route to BottomNavItem.My,
    AppRoute.Ticket.route to BottomNavItem.My,
    AppRoute.TicketRegister.route to BottomNavItem.My,
    AppRoute.TicketRegisterComplete.route to BottomNavItem.My,
    AppRoute.Point.route to BottomNavItem.My,
    AppRoute.MakeBattle.route to BottomNavItem.My,
    AppRoute.DiscussionHistory.route to BottomNavItem.My,
    AppRoute.ContentActivity.route to BottomNavItem.My,
    AppRoute.PhilosopherType.route to BottomNavItem.My,
    AppRoute.NoticeEvent.route to BottomNavItem.My,
    AppRoute.Setting.route to BottomNavItem.My,
    AppRoute.MyClass.route to BottomNavItem.Class,
    AppRoute.ClassDetail.route to BottomNavItem.Class,
    AppRoute.ClassMember.route to BottomNavItem.Class,
    AppRoute.ClassResult.route to BottomNavItem.Class,
    AppRoute.ClassMyOpinion.route to BottomNavItem.Class
)

private val entryPathBottomBarRoutes = setOf(
    AppRoute.Point.route,
    AppRoute.MakeBattle.route,
    AppRoute.NoticeEvent.route
)

fun NavController.navigateToTab(item: BottomNavItem) {
    if (!popUpToTabScreen()) {
        navigate(BottomNavItem.Home.route) { popUpTo(0) }
    }
    if (currentBackStackEntry?.tab() == item) {
        popBackStack(item.destinationRoute, inclusive = false)
    } else {
        navigate(item.route) {
            popUpTo(BottomNavItem.Home.route) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }
}

fun NavController.popUpToTabScreen(): Boolean {
    if (runCatching { getBackStackEntry(BottomNavItem.Home.route) }.isFailure) return false
    while (currentBackStackEntry?.tab() == null) {
        if (!popBackStack()) return false
    }
    return true
}

private val BottomNavItem.destinationRoute: String
    get() = if (this == BottomNavItem.TodayBattle) AppRoute.TodayBattle.route else route

private fun NavBackStackEntry.tab(): BottomNavItem? =
    bottomBarTab() ?: BottomNavItem.fromRoute(destination.route)

internal fun NavBackStackEntry.bottomBarTab(): BottomNavItem? {
    val route = destination.route
    val showsBottomBar = route !in entryPathBottomBarRoutes ||
        arguments?.getBoolean(AppRoute.ARG_SHOWS_BOTTOM_BAR) == true
    return bottomBarTabByRoute[route]?.takeIf { showsBottomBar }
}